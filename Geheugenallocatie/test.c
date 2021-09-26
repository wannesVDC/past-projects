#define TEST(expr) if (!(expr)) { fail(__FILE__, __LINE__, #expr); }

#include <inttypes.h>

static void fail(char *file, int line, char *cond){
  	fprintf(stderr, "%s:%d: Test '%s' failed\n", file, line, cond);
}

static void test_list_init(void){
	struct list list;

	list_init(&list);

  	TEST(list.first == NULL);
  	TEST(list.last == NULL);
}

/*
TEST FUNCTIONS FOR 'list_contains(const struct list *list, const struct block *block)'
LC

-list with one item
-list wiht a couple of items
-list is empty
-list doesn't contain it
*/


bool onlyItemLC(void){
	struct list *list = malloc(sizeof(struct list));
	list_init(list);
	struct block *item = malloc(sizeof(struct block));
	item->address = malloc(sizeof(uint8_t));

	list->first = item;
	list->last = item;

	return list_contains(list,item);
}

bool multipleItemsLC(void){
	struct list *list = malloc(sizeof(struct list));
	list_init(list);
	struct block *item1 = malloc(sizeof(struct block));
	struct block *item2 = malloc(sizeof(struct block));
	struct block *item3 = malloc(sizeof(struct block));
	struct block *item4 = malloc(sizeof(struct block));
	struct block *item5 = malloc(sizeof(struct block));
	struct block *item6 = malloc(sizeof(struct block));
	struct block *item7 = malloc(sizeof(struct block));

	item1->address = malloc(sizeof(uint8_t));
	item2->address = malloc(sizeof(uint8_t));
	item3->address = malloc(sizeof(uint8_t));
	item4->address = malloc(sizeof(uint8_t));
	item5->address = malloc(sizeof(uint8_t));
	item6->address = malloc(sizeof(uint8_t));
	item7->address = malloc(sizeof(uint8_t));

	item1->prev = NULL;
	item1->next = item2;

	item2->prev = item1;
	item2->next = item3;

	item3->prev = item2;
	item3->next = item4;

	item4->prev = item3;
	item4->next = item5;

	item5->prev = item4;
	item5->next = item6;

	item6->prev = item5;
	item6->next = item7;

	item7->prev = item6;
	item7->next = NULL;

	list->first = item1;
	list->last = item7;

	return list_contains(list,item5);
}

bool emptyListLC(void){
	struct list *list = malloc(sizeof(struct list));
	list_init(list);
	struct block *item = malloc(sizeof(struct block));
	item->address = malloc(sizeof(uint8_t));

	return !list_contains(list,item);
}

bool notInListLC(void){
	struct list *list = malloc(sizeof(struct list));
	list_init(list);
	struct block *item1 = malloc(sizeof(struct block));
	struct block *item2 = malloc(sizeof(struct block));
	struct block *item3 = malloc(sizeof(struct block));
	struct block *item4 = malloc(sizeof(struct block));
	struct block *item5 = malloc(sizeof(struct block));
	struct block *item6 = malloc(sizeof(struct block));
	struct block *item7 = malloc(sizeof(struct block));
	struct block *item = malloc(sizeof(struct block));

	item1->address = malloc(sizeof(uint8_t));
	item2->address = malloc(sizeof(uint8_t));
	item3->address = malloc(sizeof(uint8_t));
	item4->address = malloc(sizeof(uint8_t));
	item5->address = malloc(sizeof(uint8_t));
	item6->address = malloc(sizeof(uint8_t));
	item7->address = malloc(sizeof(uint8_t));
	item->address = malloc(sizeof(uint8_t));

	item1->prev = NULL;
	item1->next = item2;

	item2->prev = item1;
	item2->next = item3;

	item3->prev = item2;
	item3->next = item4;

	item4->prev = item3;
	item4->next = item5;

	item5->prev = item4;
	item5->next = item6;

	item6->prev = item5;
	item6->next = item7;

	item7->prev = item6;
	item7->next = NULL;

	list->first = item1;
	list->last = item7;

	return !list_contains(list,item);
}

void list_contains_test(void){

	assert(onlyItemLC());
	assert(multipleItemsLC());
	assert(emptyListLC());
	assert(notInListLC());
}


/*
TEST FUNCTIONS FOR 'list_get_length(const struct list *list)'
LGL

- empty list
- 1 item
- some items
 */

uint32_t emptyLGL(void){
	struct list *list = malloc(sizeof(struct list));
	list_init(list);

	return list_get_length(list);
}

uint32_t oneLGL(void){
	struct list *list = malloc(sizeof(struct list));
	list_init(list);
	struct block *item = malloc(sizeof(struct block));
	item->address = malloc(sizeof(uint8_t));

	list->first = item;
	list->last = item;

	return list_get_length(list);

}

uint32_t someLGL(void){
	struct list *list = malloc(sizeof(struct list));
	list_init(list);
	struct block *item1 = malloc(sizeof(struct block));
	struct block *item2 = malloc(sizeof(struct block));
	struct block *item3 = malloc(sizeof(struct block));
	struct block *item4 = malloc(sizeof(struct block));
	struct block *item5 = malloc(sizeof(struct block));
	struct block *item6 = malloc(sizeof(struct block));
	struct block *item7 = malloc(sizeof(struct block));

	item1->address = malloc(sizeof(uint8_t));
	item2->address = malloc(sizeof(uint8_t));
	item3->address = malloc(sizeof(uint8_t));
	item4->address = malloc(sizeof(uint8_t));
	item5->address = malloc(sizeof(uint8_t));
	item6->address = malloc(sizeof(uint8_t));
	item7->address = malloc(sizeof(uint8_t));

	item1->prev = NULL;
	item1->next = item2;

	item2->prev = item1;
	item2->next = item3;

	item3->prev = item2;
	item3->next = item4;

	item4->prev = item3;
	item4->next = item5;

	item5->prev = item4;
	item5->next = item6;

	item6->prev = item5;
	item6->next = item7;

	item7->prev = item6;
	item7->next = NULL;

	list->first = item1;
	list->last = item7;

	return list_get_length(list);

}


void list_get_length_test(void){

	assert(emptyLGL() == 0);
	assert(oneLGL() == 1);
	assert(someLGL() == 7);
}

/*
TEST FUNCTIONS FOR 'list_print(struct list *list, const char *title)'
LP

- empty title
- empty list
- one item
- some items
*/

void noTitleLP(void){
	char *title = NULL;

	struct list *list = malloc(sizeof(struct list));
	list_init(list);
	struct block *item1 = malloc(sizeof(struct block));
	struct block *item2 = malloc(sizeof(struct block));
	struct block *item3 = malloc(sizeof(struct block));

	item1->address = malloc(sizeof(uint8_t));
	item2->address = malloc(sizeof(uint8_t));
	item3->address = malloc(sizeof(uint8_t));

	item1->prev = NULL;
	item1->next = item2;

	item2->prev = item1;
	item2->next = item3;

	item3->prev = item2;
	item3->next = NULL;

	list->first = item1;
	list->last = item3;

	list_print(list,title);
	list_print_reverse(list,title);
}
void emptyLP(void){
	char *title = "empty list";

	struct list *list = malloc(sizeof(struct list));
	list_init(list);

	list_print(list,title);
	list_print_reverse(list,title);
}
void oneLP(void){
	char *title = "1 item list";

	struct list *list = malloc(sizeof(struct list));
	list_init(list);
	struct block *item1 = malloc(sizeof(struct block));

	item1->address = malloc(sizeof(uint8_t));

	list->first = item1;
	list->last = item1;

	list_print(list,title);
	list_print_reverse(list,title);
}
void someLP(void){
	char *title = "some items list";

	struct list *list = malloc(sizeof(struct list));
	list_init(list);
	struct block *item1 = malloc(sizeof(struct block));
	struct block *item2 = malloc(sizeof(struct block));
	struct block *item3 = malloc(sizeof(struct block));

	item1->address = malloc(sizeof(uint8_t));
	item2->address = malloc(sizeof(uint8_t));
	item3->address = malloc(sizeof(uint8_t));

	item1->prev = NULL;
	item1->next = item2;

	item2->prev = item1;
	item2->next = item3;

	item3->prev = item2;
	item3->next = NULL;

	list->first = item1;
	list->last = item3;

	list_print(list,title);
	list_print_reverse(list,title);
}


void list_print_test(void){
	noTitleLP();
	emptyLP();
	oneLP();
	someLP();
}

/*
TEST FUNCTION FOR '*list_find_block_by_address(const struct list *list, const uint8_t *address)'
LFBBA

- exact address
- address tussenin
- niet voorkomend address
- laatste block
*/
void exactLFBBA(void){
	memory_initialize();
	struct block *block = free_list.first->next;

	assert(list_find_block_by_address(&free_list, block->address) == block);
}
void midAddressLFBBA(void){
	memory_initialize();
	struct block *block = free_list.first->next;

	assert(list_find_block_by_address(&free_list, block->address+BLOCK_SIZE-1) == block);
}
void noBlockLFBBA(void){
	memory_initialize();
	uint8_t *address = free_list.last->address;
	assert(list_find_block_by_address(&free_list, address + 2* BLOCK_SIZE) == NULL);
}

void lastBlockLFBBA(void){
	memory_initialize();
	uint8_t *address = free_list.last->address;
	assert(list_find_block_by_address(&free_list, address) != NULL);
}

void list_find_block_by_address_test(void){
	exactLFBBA();
	midAddressLFBBA();
	noBlockLFBBA();
	lastBlockLFBBA();
}

/*
TEST FUNCTION FOR 'blocks_are_contiguous(const struct block *left, const struct block *right)'
BAC

- true
- left en right wisselen
- false
*/

bool trueBAC(void){
	struct block *left = malloc(sizeof(struct block));
	struct block *right = malloc(sizeof(struct block));

	left->address = malloc(sizeof(uint8_t));
	right->address = left->address + BLOCK_SIZE;

	return blocks_are_contiguous(left,right);
}

bool switchBAC(void){
	struct block *left = malloc(sizeof(struct block));
	struct block *right = malloc(sizeof(struct block));

	left->address = malloc(sizeof(uint8_t));
	right->address = left->address + BLOCK_SIZE;

	return blocks_are_contiguous(right,left);
}

bool falseBAC(void){
	struct block *left = malloc(sizeof(struct block));
		struct block *right = malloc(sizeof(struct block));

		left->address = malloc(sizeof(uint8_t));
		right->address = left->address + (BLOCK_SIZE/2);

		return blocks_are_contiguous(left,right);
}


void blocks_are_contiguous_test(void){
	assert(trueBAC());
	assert(switchBAC);
	assert(falseBAC);
}


/*
TEST FUNCTION FOR 'required_number_of_contiguous_blocks(uint32_t size)'
RNOC

- 65 (answer 2)
- 64 (answer 1)
- 22 (answer 1)
*/

uint32_t RNOC65(void){
	return required_number_of_contiguous_blocks(65);
}
uint32_t RNOC64(void){
	return required_number_of_contiguous_blocks(64);
}
uint32_t RNOC22(void){
	return required_number_of_contiguous_blocks(22);
}


void required_number_of_contiguous_blocks_test(void){
	assert(RNOC65() == 2);
	assert(RNOC64() == 1);
	assert(RNOC22() == 1);
}


/*
TEST FUNCTION FOR 'has_number_of_contiguous_blocks(const struct block *block, uint32_t)'
HNOCB

- not enough blocks
- not contiguous
- true
*/
bool notEnoughHNOCB(void){
	struct block *item1 = malloc(sizeof(struct block));
	struct block *item2 = malloc(sizeof(struct block));
	struct block *item3 = malloc(sizeof(struct block));

	item2->address = malloc(sizeof(uint8_t));
	item3->address = item2->address + BLOCK_SIZE;

	item1->prev = NULL;
	item1->next = item2;

	item2->prev = item1;
	item2->next = item3;

	item3->prev = item2;
	item3->next = NULL;

	return !has_number_of_contiguous_blocks(item2,4);
}
bool notContiguousHNOCB(void){
	struct block *item1 = malloc(sizeof(struct block));
	struct block *item2 = malloc(sizeof(struct block));
	struct block *item3 = malloc(sizeof(struct block));
	struct block *item4 = malloc(sizeof(struct block));
	struct block *item5 = malloc(sizeof(struct block));

	item2->address = malloc(sizeof(uint8_t));
	item3->address = item2->address + BLOCK_SIZE;
	item4->address = item3->address + BLOCK_SIZE + 10;

	item1->prev = NULL;
	item1->next = item2;

	item2->prev = item1;
	item2->next = item3;

	item3->prev = item2;
	item3->next = item4;

	item4->prev = item3;
	item4->next = item5;

	item5->prev = item4;
	item5->next = NULL;

	return !has_number_of_contiguous_blocks(item2, 3);
}
bool trueHNOCB(void){
	struct block *item1 = malloc(sizeof(struct block));
	struct block *item2 = malloc(sizeof(struct block));
	struct block *item3 = malloc(sizeof(struct block));
	struct block *item4 = malloc(sizeof(struct block));
	struct block *item5 = malloc(sizeof(struct block));

	item2->address = malloc(sizeof(uint8_t));
	item3->address = item2->address + BLOCK_SIZE;
	item4->address = item3->address + BLOCK_SIZE;
	item5->address = item4->address + BLOCK_SIZE;

	item1->prev = NULL;
	item1->next = item2;

	item2->prev = item1;
	item2->next = item3;

	item3->prev = item2;
	item3->next = item4;

	item4->prev = item3;
	item4->next = item5;

	item5->prev = item4;
	item5->next = NULL;

	return has_number_of_contiguous_blocks(item2, 4);
}

void has_number_of_contiguous_blocks_test(void){
	assert(notEnoughHNOCB());
	assert(notContiguousHNOCB());
	assert(trueHNOCB());
}

/*
TEST FUNCTION FOR 'list_init_block(struct list *list, struct block *block, uint8_t *address)'
LIB

-unsure what tests to do --> leave for later TODO
*/


/*
TEST FUNCTION FOR 'list_insert_chain(struct list *list, struct block *block)'
LIC

- 1 item chain
- some items chain
*/


bool oneItemChainLIC(void){
	struct list *list = malloc(sizeof(struct list));
	list_init(list);

	struct block *block = malloc(sizeof(struct block));
	block->prev = NULL;
	block->next = NULL;

	list_insert_chain(list,block);

	if (list->first == block && list->last == block)
	{
		return true;
	}
	return false;
}

bool someItemChainLIC(void){
	struct list *list = malloc(sizeof(struct list));
	list_init(list);

	struct block *block1 = malloc(sizeof(struct block));
	struct block *block2 = malloc(sizeof(struct block));
	struct block *block3 = malloc(sizeof(struct block));
	struct block *block4 = malloc(sizeof(struct block));


	block1->prev = NULL;
	block1->next = block2;

	block2->prev = block1;
	block2->next = block3;

	block3->prev = block2;
	block3->next = block4;

	block4->prev = block3;
	block4->next = NULL;

	list_insert_chain(list,block1);

	if (list->first == block1 && list->last == block4)
	{
		return true;
	}
	return false;
}


void list_insert_chain_test(void){
	assert(oneItemChainLIC());
	assert(someItemChainLIC());
}

/*
TEST FUNCTIONS FOR 'list_remove_chain(struct list *list, struct block *block, uint32_t block_count)'
LRC

- chain is shorter than block_count
- chain is longer than block_count
*/

void shorterLRC(void){
	struct list *list = malloc(sizeof(struct list));
	list_init(list);

	struct block *item1 = malloc(sizeof(struct block));
	struct block *item2 = malloc(sizeof(struct block));
	struct block *item3 = malloc(sizeof(struct block));
	struct block *item4 = malloc(sizeof(struct block));
	struct block *item5 = malloc(sizeof(struct block));

	// chain 2 items
	item1->address = malloc(sizeof(uint8_t));
	item2->address = item1->address + BLOCK_SIZE;
	item3->address = item2->address + (3*BLOCK_SIZE);

	item1->prev = NULL;
	item1->next = item2;
	item2->prev = item1;
	item2->next = item3;
	item3->prev = item2;
	item3->next = item4;
	item4->prev = item3;
	item4->next = item5;
	item5->prev = item4;
	item5->next = NULL;

	assert(list_remove_chain(list,item1,3) == 2);
	assert(!list_contains(list,item1));
	assert(!list_contains(list,item2));
	assert(list_contains(list,item3));
}
void longerLRC(void){
	struct list *list = malloc(sizeof(struct list));
	list_init(list);

	struct block *item1 = malloc(sizeof(struct block));
	struct block *item2 = malloc(sizeof(struct block));
	struct block *item3 = malloc(sizeof(struct block));
	struct block *item4 = malloc(sizeof(struct block));
	struct block *item5 = malloc(sizeof(struct block));

	// chain 2 items
	item1->address = malloc(sizeof(uint8_t));
	item2->address = item1->address + BLOCK_SIZE;
	item3->address = item2->address + BLOCK_SIZE;

	item1->prev = NULL;
	item1->next = item2;
	item2->prev = item1;
	item2->next = item3;
	item3->prev = item2;
	item3->next = item4;
	item4->prev = item3;
	item4->next = item5;
	item5->prev = item4;
	item5->next = NULL;

	assert(list_remove_chain(list,item1,2) == 2);
	assert(!list_contains(list,item1));
	assert(!list_contains(list,item2));
	assert(list_contains(list,item3));
}

void list_remove_chain_test(void){
	shorterLRC();
	longerLRC();
}


/*
TEST FUNCTIONS FOR 'memory_available()'
MA

- after initialize (moet HEAP_SIZE zijn)
- na een paar blokken gebruiken
- leeg
*/
void initMA(void){
	memory_initialize();
	assert(memory_available() == HEAP_SIZE);
}
void someBlocksMA(void){
	memory_initialize();
	struct block *block = free_list.first;
	block = block->next->next;

	list_remove_chain(&free_list,block,3);
	list_insert_chain(&used_list,block);

	assert(memory_available() == (NUMBER_OF_BLOCKS - 3)*BLOCK_SIZE);
}
void emptyMA(void){
	memory_initialize();
	struct block *block = free_list.first;

	list_remove_chain(&free_list,block,NUMBER_OF_BLOCKS);
	list_insert_chain(&used_list,block);

	assert(memory_available() == 0);
}

void memory_available_test(void){
	initMA();
	someBlocksMA();
	emptyMA();
}

/*
TEST FUNCTIONS FOR 'memory_used()'
MU

- empty
- some blocks
- full
*/

void emptyMU(void){
	memory_initialize();
	assert(memory_used() == 0);
}
void someBlocksMU(void){
	memory_initialize();
	struct block *block = free_list.first;
	block = block->next->next;

	list_remove_chain(&free_list,block,3);
	list_insert_chain(&used_list,block);

	assert(memory_used() == 3*BLOCK_SIZE);
}
void fullMU(void){
	memory_initialize();
	struct block *block = free_list.first;

	list_remove_chain(&free_list,block,NUMBER_OF_BLOCKS);
	list_insert_chain(&used_list,block);

	assert(memory_used() == HEAP_SIZE);
}

void memory_used_test(void){
	emptyMU();
	someBlocksMU();
	fullMU();
}

/*
TEST FUNCTIONS FOR '*memory_allocate(uint32_t size)'
MA

- not enough blocks
- non contiguous blocks
- valid
*/

void notEnoughMA(void){
	memory_initialize();
	list_remove_chain(&free_list,free_list.first->next->next,NUMBER_OF_BLOCKS);

	assert(memory_allocate(4*BLOCK_SIZE) == NULL);
}
void notContiguousMA(void){
	memory_initialize();
	list_remove_chain(&free_list,free_list.first->next->next->next,NUMBER_OF_BLOCKS);// we hebben nog 3 blokken
	list_remove_chain(&free_list, free_list.first->next,1); //only blocks 1 and 3 remain

	assert(memory_allocate(2*BLOCK_SIZE) == NULL);
}
void validMA(void){
	memory_initialize();
	struct block * pter = memory_allocate(7*BLOCK_SIZE);
	assert(pter->alloc_count == 7);
	assert(list_get_length(&free_list) == NUMBER_OF_BLOCKS - 7);
	assert(list_get_length(&used_list) == 7);
}

void memory_allocate_test(void){
	notEnoughMA();
	notContiguousMA();
	validMA();
}

/*
TEST FUNCTIONS FOR 'memory_release(void *ptr)'
MR

- NULL
- Memory not allocated
- 1 block
- valid
*/

void nullMR(void){
	assert(!memory_release(NULL));
}
void notAllocatedMR(void){
	memory_initialize();
	struct block *block = free_list.first->next->next;
	list_remove_chain(&free_list, block, 3*BLOCK_SIZE);
	list_insert_chain(&used_list, block);
	assert(!memory_release(block));
}
void oneBlockMR(void){
	memory_initialize();
	void *ptr = memory_allocate(1);
	assert(memory_release(ptr));
}
void validMR(void){
	memory_initialize();
	void *ptr = memory_allocate(3*BLOCK_SIZE);
	assert(memory_release(ptr));
}

void memory_release_test(void){
	nullMR();
	notAllocatedMR();
	oneBlockMR();
	validMR();
}

static void print_bookkeeping(const char *title);

// test uitbreiding:
static void test_print_bookkeeping(void){
	void *p1,*p2,*p3,*p4,*p5;
	memory_initialize();

	p1 = memory_allocate(1);				//1 block used
	p2 = memory_allocate(BLOCK_SIZE);		//1
	p3 = memory_allocate(2*BLOCK_SIZE+1);	//3
	p4 = memory_allocate(3*BLOCK_SIZE);		//3
	p5 = memory_allocate(3*BLOCK_SIZE+10);	//4, som is 12
	memory_release(p3);
	print_bookkeeping("current bookkeeping");

	memory_release(p1);
	memory_release(p2);
	memory_release(p4);
	memory_release(p5);
}


void memory_test(void){
  	assert(sizeof(heap) == HEAP_SIZE);

  	printf("Heap information:\n");
  	printf("  heap size       : %lu bytes\n", sizeof(heap));
  	printf("  block size      : %u bytes\n", BLOCK_SIZE);
  	printf("  number of blocks: %d\n", NUMBER_OF_BLOCKS);
  	printf("  start address   : %p\n", heap);
  	printf("  end address     : %p\n", &(heap[HEAP_SIZE]));

  	assert(NUMBER_OF_BLOCKS > 2);
  	assert((NUMBER_OF_BLOCKS * BLOCK_SIZE) == sizeof(heap));

  	test_list_init();


  	/*
  	HIER ALLE CALLS NAAR ZELFGEMAAKTE TESTS
  	*/
  	printf("\n");

  	list_contains_test();
  	list_get_length_test();
  	//list_print_test();
  	list_find_block_by_address_test();
  	blocks_are_contiguous_test();
  	required_number_of_contiguous_blocks_test();
  	has_number_of_contiguous_blocks_test();
  	list_insert_chain_test();
  	list_remove_chain_test();
  	memory_available_test();
	memory_used_test();
  	memory_allocate_test();
  	memory_release_test();
	test_print_bookkeeping();

  	printf("done\n");
}