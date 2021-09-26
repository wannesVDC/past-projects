package jumpingalien.model;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;
/**
 * A class of schools which can contain students of type T which are a subclass.
 * 
 * @Invar	TODO associations
 * 
 * @authors  Wannes Vande Cauter, Nils Van Dessel	--	Fysica
 */
public class School<T extends Organism & Identifiable> {
	
	private Comparator<T> myComparator = new Comparator<T>()
					{
						/**
						 * Returns an integer based on the id's of the 2 given organisms.
						 * 
						 * @param	o1
						 * 			A first organism.
						 * @param	o2
						 * 			A second organism.
						 * @return	Returns the difference between the id's of the 2 given organisms.
						 * 			|result == (int) ( o1.getId() - o2.getId() )
						 */
					@Override
					public int compare(T o1, T o2) {
						if (!( this.inValidArgument(o1) || this.inValidArgument(o2) )) {
							long ret = o1.getId()-o2.getId();
							return (int) ret;
						}
						else {
							return -1;
							}
						}
					
					/**
					 * Returns whether the organism invalid (null).
					 * 
					 * @param 	org
					 * 			The Organism to check for being valid.
					 * @return	Returns true if the organism is invalid, otherwise false.
					 * 			|result == (org == null)
					 */
					public boolean inValidArgument(T org) {
						return (org == null);
						}
					};
					
	private TreeSet<T> students = new TreeSet<>(this.getMyComparator());
	
	private World world;
	private boolean alive = true;
	
	/**
	 * Initialize a new school with given world. 
	 * 
	 * @param 	world
	 * 			The world in which the school is created.
	 * @post	The given world is the world of this school
	 * 			|new.getWorld == world
	 * @post 	The school is added to the world if it is not null.
	 * 			|if (world != null)
	 * 			|then world.addschool(this)
	 * @throws 	IllegalArgumentException
	 * 			An IllegalArgumentException is thrown if the given world is null.
	 * 			|if (world == null)
	 * 			|then throw new IllegalArgumentException
	 */
	@Raw
	public School(World world) {
		this.setWorld(world);
		if (world != null) {
			world.addSchool(this);
		}
		
	}
	
	/**
	 * The world of the school gets set.
	 * 
	 * @param 	world
	 * 			The new world of the school.
	 * @post	The new world of the school is world.
	 * 			|new.getWorld() == world
	 */
	@Raw
	private void setWorld(World world) {
		this.world = world;
	}
	
	public boolean isAlive() {
		return this.alive;
	}
	
	/**
	 * Returns the school in which the school is located.
	 */
	@Basic
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Returns the set of students.
	 */
	@Basic
	public Set<T> getStudents() {
		Set<T> ret = new TreeSet<>(this.getMyComparator());
		ret.addAll(this.students);
		return ret;
	}
	
	/**
	 * Returns the Comparator used to define the treeSet.
	 */
	@Basic
	private Comparator<T> getMyComparator() {
		return this.myComparator;
	}

	/**
	 * Return the number of students in this school.
	 * 
	 * @return 	0 will be returned if this.getstudents equals null.
	 * 			|if (this.getStudents() == null)
	 * 			|then result == 0
	 * @return	this.getStudents().size() will be returned if this.getstudents does not equal null
	 * 			|if (this.getStudents() != null)
	 * 			|then result == this.getStudents().size()
	 */
	public int getNBOfStudents() {
		if (this.getStudents() == null) {
			return 0;
		}
		else {
			return this.getStudents().size();
		}
	}
	
	/**
	 * Returns whether a given student is in  the school.
	 * 
	 * @param 	student
	 * 			The students who might be in this school.
	 * @return 	Returns true if the student is in this.getStudents and false otherwise.
	 * 			|result == this.getStudents().contains(student)
	 */
	public boolean contains(T student) {
		return this.getStudents().contains(student);
	}
	
	/**
	 * Removes a given student from the school.
	 * 
	 * @param	student
	 * 			The student who should be removed from the school.
	 * TODO	
	 * @post 	The given student is no longer in this school.
	 * 			|new.contains(student) == false 
	 */
	@Raw
	public void removeStudent(T student) {
		//((Organism) student).loseHP(this.getNBOfStudents()-1);
		//this.damageBy1();;
		this.students.remove(student);
		//if (student != null) {
		//	((Organism) student).loseHP(this.getNBOfStudents()-1);
		//	this.damageBy1();
		//}
	}
	
	/**
	 * Adds the student to the list of students if the student is not null, otherwise throws an IllegalArgumentException
	 * 
	 * @param 	student
	 * 			The student to be added to the school.
	 * @throws 	IllegalArgumentException
	 * 			This exception gets thrown if student is null.
	 * 			|if (student == null)
	 * 			|then throw new IllegalAgrumentException
	 */
	@Raw
	public void addStudent(T student) throws IllegalArgumentException {
		if (student == null) {
			throw new IllegalArgumentException();
		}
		this.students.add(student);
	}
	
	/**
	 * The school gets terminated.
	 * 
	 * @effect 	The school gets removed from the world it belongs to.
	 * 			|this.getWorld().terminateSchool(this)
	 * @post	The world is set to null
	 * 
	 */
	@Raw
	public void terminate() {
		this.alive = false;
		for (T org : this.getStudents()) {
			org.terminate();
		}
		this.getWorld().terminateSchool(this);
		this.setWorld(null);
		
	}
	
	/**
	 * Add a new world to the school.
	 * 
	 * @param 	world2
	 * 			The world to add to the school.
	 */
	public void addWorld(World world2) {
		if (this.getWorld() != world2) {
			this.setWorld(world2);
		}
	}
}