#include "memory_priv.h"
#include <inttypes.h>

/* Initializes the given list to be the empty list.
 *
 * Preconditions:
 *  - the given list is a valid pointer to an object of type struct list
 *
 * This function is already implemented for you.
 */
static void list_init(struct list *list){
	list->first = NULL;
	list->last = NULL;
}

/* Returns true when the given block is valid.
 *
 * This function is already implemented for you.
 */
static bool block_is_valid(const struct block *block){
	return (   (block != NULL)
		&& (block->address >= heap)
		&& (block->address < &(heap[HEAP_SIZE]))
	);
}

/* Returns true if the given list contains the given block. Returns false
 * otherwise.
 */
static bool list_contains(const struct list *list, const struct block *block){
	if (list->first == NULL){
		return false;
	}
	struct block * current = list->first;

	if (current->address == block->address){
		return true;
	}

	while (current->next != NULL){
		if (current->address == block->address){
			return true;
		}
		current = current->next;
	}
	return false;
}

/* Returns the length of the given list (the number of blocks it contains) */
static uint32_t list_get_length(const struct list *list){
	if (list->first == NULL){
		return 0;
	}
  
	int counter = 1;
	struct block * current = list->first;
	while (current->next != NULL){
		++counter;
		current = current->next;
	}
	return counter;
}

/* Prints a human representation of the given list in forward order.
 *
 * Expected format:
 *
 *   Assuming a value for title of "free list", a list with two elements, with
 *   addresses 0x559cd4da1040 and 0x559cd4da1060 should be printed as follows:
 *
 *     free list:
 *       0x559cd4da1040->0x559cd4da1060->NULL
 *
 * Hint: the %p printf modifier can be used to print the value of a pointer
 */
static void list_print(struct list *list, const char *title){
	if (title == NULL){
		printf("untitled");
	}
	else{
		printf("%s",title);
	}
	printf(" :\n");
	if (list->first == NULL){
		printf("NULL\n");
	}
	else{
		struct block * current = list->first;
		printf("%p", current->address);
		while (current->next != NULL){
			current = current->next;
			printf("->");
			printf("%p", current->address);
		}
		printf("->NULL \n");
	}
}

/* Prints a human representation of the given list in reverse order.
 *
 * The same format as for the function "list_print" is expected.
 */
static void list_print_reverse(struct list *list, const char *title){
	if (title == NULL){
			printf("untitled");
	}
	else{
		printf("%s",title);
	}
	printf(" :\n");
	if (list->last == NULL){
		printf("NULL\n");
	}
	else{
		struct block * current = list->last;
		printf("%p", current->address);
		while (current->prev != NULL){
			current = current->prev;
			printf("->");
			printf("%p", current->address);
		}
		printf("->NULL\n");
	}
}

/* Returns the block for which the given address falls within its address
 * range. The address range of a block starts with its address and ends
 * with its address + BLOCK_SIZE - 1.
 *
 * If the given list does not contain the block, returns NULL.
 */
static struct block *list_find_block_by_address(const struct list *list,
                                                const uint8_t *address){
	if (list->first != NULL){
		struct block * current = list->first;
		while (current != NULL){
			if (address >= current->address && address < (current->address + BLOCK_SIZE)){
				return current;
			}
      		current = current->next;
		}
	}
	return NULL;
}

/* Returns true when the two given blocks are contiguous.
 *
 * Two blocks, left and right, are contiguous when
 *    1) the value of left's address field is less than the value of right's
 *    address field and 2) adding BLOCKS_SIZE to the value of left's address
 *    field gives the value of right's address field.
 */
static bool blocks_are_contiguous(const struct block *left,
                                  const struct block *right){
	if (left->address == NULL || right->address == NULL){
		return false;
	}
	
	if (left->address < right->address){
		if (left->address + BLOCK_SIZE == right->address){
			return true;
		}
	}
	return false;
}

/* Returns the number of contiguous blocks that is required to satisfy
 * an allocation request of size bytes.
 */
static uint32_t required_number_of_contiguous_blocks(uint32_t size){
	int div = 0;
	if (size%BLOCK_SIZE > 0){			// Altijd 1 meer nodig wanneer er een kommagetal is
		div = 1;
	}
	return (size/BLOCK_SIZE + div);
}


/* - Returns true when count equals zero.
* - Returns true when the given block has at least (count-1) number of
* successors and the first (count-1) successors of the given block are all
* contiguous with respect to their predecessor.
* - Returns false otherwise.
*/
static bool has_number_of_contiguous_blocks(const struct block *block,
                                            uint32_t            count){
	if (count <= 1){ // count == 1   ==> block needs 0 successors ==> return true;
		return true;
	}
	if(block->next != NULL){
		if (has_number_of_contiguous_blocks(block->next,count-1) && blocks_are_contiguous(block, block->next)){
			return true;
		}
	}
	return false;
}

/* Initializes the given block with the given address and appends it to the
 * given list.
 *
 * Preconditions:
 *   - the given list is ordered by ascending address
 *   - when the list is not empty
 *     - the given address is greater than the address of the last block in
 *       the given list:
 *           address > list->last->address
 *     - the given address is BLOCK_SIZE bytes greater than the address of the
 *       last block in the given list:
 *           list->last->address + BLOCK_SIZE = address
 *
 * Postconditions:
 *   - the resulting list is ordered by ascending address
 *   - the last block in the resulting is the given block
 *   - the value of block->alloc_count is zero
 */
static void list_init_block(struct list  *list,
                            struct block *block,
                            uint8_t      *address){
	if (list->last == NULL){
		list->first = block;
		list->last = block;
		block->alloc_count = 0;
		block->next = NULL;
		block->prev = NULL;
		block->address = address;
	}

	else{
		struct block *current = list->last;

		if (current->address != address){ // check wether the block is already in de list
			current->next = block;
			block->prev = current;
			block->next = NULL;
			block->alloc_count = 0;
			block->address = address;
			list->last = block;
		}
	}
}

/* Inserts a chain of blocks starting with the given block in the given list.
 *
 * Preconditions:
 *   - the given list is ordered by ascending address
 *   - no block in the chain of blocks that starts with the given block is
 *     already an element of the given list
 *   - the blocks in the given chain of blocks are ordered by ascending
 *     address
 *   - all blocks in the given chain of blocks are contiguous
 *
 * Postconditions:
 *   - the given list is ordered by ascending address
 *   - the given list contains the given chain of blocks
 */
static void list_insert_chain(struct list* list, struct block *block){
	struct block * current = block;
	while(current->next != NULL){
		current = current->next;
	}

	if (list->first == NULL){
		list->first = block;
		list->last = current;
	}

	else{
		struct block *curList = list->first;
		while (curList->address < block->address && curList->next != NULL){
			curList = curList->next;
		}
		if (curList->next == NULL && curList->address < block->address){
			list->last = current;
			curList->next = block;
			block->prev = curList;
			current->next = NULL;
		}
		else if (curList->address > block->address){
			if (curList->prev == NULL){
				list->first = block;

			}
			else{
				curList->prev->next = block;				
			}
			block->prev = curList->prev;
			current->next = curList;
			curList->prev = current;
		}
	}
}

/* Removes a chain of blocks starting with the given block from the given list,
 * without disconnecting the individual links of the chain. At most
 * block_count blocks will be removed. This functions aims to remove
 * as many blocks as possible with block_count as the maximum.
 *
 * Preconditions:
 *   - the given block is an element of the given list
 *   - all blocks in the given chain of blocks are contiguous up to
 *     block_count or up to the end of the chain if the length of the chain
 *     is less than block_count.
 *
 * Returns the number of blocks that are actually removed.
 */
static uint32_t list_remove_chain(struct list  *list,
                                  struct block *block,
                                  uint32_t      block_count){
	uint32_t counter = 1; //block zelf al
	struct block *current = block;
	while(current->next != NULL && counter < block_count){
		if (blocks_are_contiguous(current,current->next)){
			counter++;
		}
		else{
			if (block->prev != NULL){
				block->prev->next = current->next;
			}
			else{
				list->first = current->next;
			}
			
			if(list->last == current){
				list->last = block->prev;
			}
			if (current->next != NULL){
				current->next->prev = block->prev;
			}
			block->prev = NULL;
			current->next = NULL;
			return counter;
		}
		current = current->next;
	}
	if(block->prev != NULL){
		block->prev->next = current->next;
	}
	else
	{
		list->first = current->next;
	}

	if(list->last == current){
		list->last = block->prev;
	}
	if (current->next != NULL){
		current->next->prev = block->prev;
	}
	block->prev = NULL;
	current->next = NULL;

	return counter;
}

/* Initializes the dynamic memory and its bookkeeping.
 *
 * This function is already implemented for you. Study this function
 * thoroughly in order to understand how the heap and its bookkeeping
 * is represented.
 */
void memory_initialize(void){
	list_init(&free_list);
	list_init(&used_list);

	uint8_t *address = heap;
	for (int i=0; i < (sizeof(pool_of_blocks)/sizeof(pool_of_blocks[0])); i++){
		list_init_block(&free_list, &(pool_of_blocks[i]), address);
		address += BLOCK_SIZE;
	}
	assert(address == &(heap[HEAP_SIZE]));
}

/* Returns the amount of dynamic memory available in number of bytes */
uint32_t memory_available(void){
	uint32_t memFree = list_get_length(&free_list) * BLOCK_SIZE;
	return memFree;
}

/* Returns the amount of dynamic memory used in number of bytes */
uint32_t memory_used(void){
	uint32_t memUsed = list_get_length(&used_list) * BLOCK_SIZE;
	return memUsed;
}



// for the next function
static bool canBeAllocated(struct block * start, uint32_t numberOfBlocks){ // hulpfunctie	
	struct block * current = start;

	for(int i=1; i<numberOfBlocks; i++){ // i=1, want de start is al een vd blokken dat in orde is
		if(current->next == NULL){
			return false;
		}
		if(!blocks_are_contiguous(current, current->next)){
			return false;
		}
	}

	return true;
}

/* Allocates size number of *contiguous bytes* and returns a pointer to the
 * allocated memory. The memory does not have to be initialized.
 *
 * Returns NULL,
 *   - if size is zero,
 *   - or if not enough contiguous memory is available
 *
 * Postconditions:
 *   - After a successful allocation, the first block that represents the
 *     the beginning of the allocated memory must have its alloc_count field
 *     set to the number of contiguous blocks that was required to fulfil
 *     the allocation. This information will be useful for releasing the
 *     allocated memory.
 *
 * Hint: Don't forgot to update free_list and used_list
 * Hint: The functions list_remove_chain and list_insert_chain can be useful here
 */
void *memory_allocate(uint32_t size){
	if (size == 0){
		return NULL;
	}

	if (free_list.first != NULL){
		struct block * current = free_list.first;
		uint32_t numberOfBlocks = required_number_of_contiguous_blocks(size);

		bool alloc = false;
		while(current->next != NULL && !alloc){
			current = current->next;
			alloc = canBeAllocated(current,numberOfBlocks);
		}

		if(alloc){
			list_remove_chain(&free_list,current,numberOfBlocks);
			list_insert_chain(&used_list,current);
			current->alloc_count = numberOfBlocks;

			return current;
		}
		else{
			return NULL;
		}
	}
	return NULL;
}

/* Releases the memory pointed to by the given pointer, which must have been
 * returned by a previous call to memory_allocate.
 *
 * Returns true when actual memory has been released, false otherwise.
 *
 * Possible reasons why the memory has not been released:
 *  - The given pointer is NULL.
 *  - The given pointer does not point to memory that was allocated by
 *    memory_allocate.
 *
 * Hint: Don't forgot to update free_list and used_list
 * Hint: The functions list_remove_chain and list_insert_chain can be useful here
 */
bool memory_release(void *ptr){
	if (ptr == NULL || used_list.first == NULL){
		return false;
	}

	struct block * current = used_list.first;
	while (current->next != NULL && current != ptr){
		current = current->next;
	}
	if (current == ptr && current->alloc_count > 0){
		list_remove_chain(&used_list,current,current->alloc_count);
		current->alloc_count = 0;
		list_insert_chain(&free_list,current);

		return true;
	}

	return false;
}


/* uitbreiding */
static void print_bookkeeping(const char *title){
	printf("%s:\n",title);

	for (uint8_t *i = &heap[0]; i < (&heap[0] + HEAP_SIZE); i += BLOCK_SIZE){// loop for every block address
		printf("Block ");
		printf("%ld", (i-&heap[0])/BLOCK_SIZE); // i-heap is 0 in eerste rot, delen door block_size geeft counter van aantal keer (start met 0)
		printf(" (%p to %p)",i,(i+BLOCK_SIZE)); //address tot adres volgende
		printf(" is located in ");
		struct block *current = list_find_block_by_address(&free_list,i);
		if (current == NULL){
			current = list_find_block_by_address(&used_list,i);
			if (current == NULL){
				printf("no list");
			}
			else{
				printf("used_list");
			}
		}
		else{
			printf("free_list");
		}
		if (current != NULL)
			printf(" with alloc_count %"PRIu32".",current->alloc_count);
		printf("\n");
	}
}

