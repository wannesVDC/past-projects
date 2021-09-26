package jumpingalien.facade;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.management.MalformedObjectNameException;

import jumpingalien.model.Direction;
import jumpingalien.model.Mazub;
import jumpingalien.model.Organism;
import jumpingalien.model.Plant;
import jumpingalien.model.School;
import jumpingalien.model.Shark;
import jumpingalien.model.Skullcab;
import jumpingalien.model.Slime;
import jumpingalien.model.Sneezewort;
import jumpingalien.model.World;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;

public class Facade implements IFacade{

	@Override
	public boolean isTeamSolution() {
		return true;
	}

	@Override
	public Mazub createMazub(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
		return new Mazub(pixelLeftX,pixelBottomY, sprites);
	}
	public Sprite getCurrentSprite(Mazub alien) throws ModelException{
		return alien.getCurrentSprite();
	}

	@Override
	public double[] getActualPosition(Mazub alien) throws ModelException {
		return alien.getMeterPosition();
	}

	@Override
	public void changeActualPosition(Mazub alien, double[] newPosition){
		try {
			if (newPosition != null) {
				if(newPosition.length != 2) {
					throw new ModelException("invalid position");
				}
				else {
					alien.setMeterPosition(newPosition);
				}
			}
			else {
				throw new ModelException("invalid position");
			}
			if (Double.isNaN(newPosition[0]) || Double.isNaN(newPosition[1])) {
				throw new ModelException("position can't be NaN");
			}
		}catch (ModelException exc) {
			throw new ModelException("invalid position");
		}
	}

	@Override
	public int[] getPixelPosition(Mazub alien) throws ModelException {
		return alien.getPosition();
	}

	@Override
	public int getOrientation(Mazub alien) throws ModelException {
		return alien.getOldDirection().dirToInt();
	}

	@Override
	public double[] getVelocity(Mazub alien) throws ModelException {
		return alien.getVelocity();
	}

	@Override
	public double[] getAcceleration(Mazub alien) throws ModelException {
		return alien.getAcceleration();
	}

	@Override
	public boolean isMoving(Mazub alien) throws ModelException {
		return alien.isMoving();
	}

	@Override
	public void startMoveLeft(Mazub alien) throws ModelException {
		try {
			assert(alien.isAlive());
			alien.startMove(Direction.LEFT);
		}catch (AssertionError exc) {
			throw new ModelException(exc);
		}
	}

	@Override
	public void startMoveRight(Mazub alien) throws ModelException {
		try {
			assert(alien.isAlive());
			alien.startMove(Direction.RIGHT);
		}catch (AssertionError exc) {
			throw new ModelException(exc);
		}
	}

	@Override
	public void endMove(Mazub alien) throws ModelException {
		alien.endMove();
	}

	@Override
	public boolean isJumping(Mazub alien) throws ModelException {
		return alien.isJumping();
	}

	@Override
	public void startJump(Mazub alien) throws ModelException {
		try {
			assert(alien.isAlive());
			alien.startJump();
		}catch (AssertionError exc) {
			throw new ModelException("Mazub's dead");
		}
	}

	@Override
	public void endJump(Mazub alien) throws ModelException {
		alien.endJump();
	}
	

	@Override
	public boolean isDucking(Mazub alien) throws ModelException {
		return alien.isDucking();
	}

	@Override
	public void startDuck(Mazub alien) throws ModelException {
		alien.startDuck();
	}

	@Override
	public void endDuck(Mazub alien) throws ModelException {
		alien.endDuck();
	}
	
	public Sprite[] getSprites(Mazub alien) {
		return alien.getAllSprites();
	}

	@Override
	public World createWorld(int tileSize, int nbTilesX, int nbTilesY, int[] targetTileCoordinate,
			int visibleWindowWidth, int visibleWindowHeight, int... geologicalFeatures) throws ModelException {
		return new World(tileSize,nbTilesX,nbTilesY,targetTileCoordinate,visibleWindowWidth,visibleWindowHeight,geologicalFeatures);
	}

	@Override
	public void terminateWorld(World world) throws ModelException {
		world.terminate();
	}

	@Override
	public int[] getSizeInPixels(World world) throws ModelException {
		int x = world.getMaxPixelX();
		int y = world.getMaxPixelY();
		int[] size = {x,y};
		return size;
	}

	@Override
	public int getTileLength(World world) throws ModelException {
		return world.getTileSize();
	}

	@Override
	public int getGeologicalFeature(World world, int pixelX, int pixelY) throws ModelException {
		int[] co = {pixelX,pixelY};
		return (world.getTypeTile(world.getTileNumber(world.getTileCoordFromPixel(co)))).getNumber();
	}

	@Override
	public void setGeologicalFeature(World world, int pixelX, int pixelY, int geologicalFeature) throws ModelException {
		int[] co = {pixelX,pixelY};
		world.setTypeTile(world.getTileNumber(world.getTileCoordFromPixel(co)), geologicalFeature);
	}

	@Override
	public int[] getVisibleWindowDimension(World world) throws ModelException {
		// TODO Auto-generated method stub
		int x = world.getMaxPixelsWindowX();
		int y = world.getMaxPixelsWindowY();
		int[] dim = {x,y};
		return dim;
	}

	@Override
	public boolean hasAsGameObject(Object object, World world) throws ModelException {
		return world.hasAsGameObject(object);
	}

	@Override
	public Set<Object> getAllGameObjects(World world) throws ModelException {
		return world.getOrganism();
	}

	@Override
	public Mazub getMazub(World world) throws ModelException {
		return world.getPlayer();
	}

	@Override
	public void addGameObject(Object object, World world) throws ModelException {
		try {
			assert(!(object instanceof Mazub && world.hasPlayer()));
			assert(object != null);
			assert(((Organism) object).isAlive());
			assert(world.getNbOfObjects() < world.getMaxNumberOfObjects() || object instanceof Mazub);
			world.addObject((Organism) object);
			try {
				assert(((Organism) object).canSpawn(this.getPixelPosition(object)[0],this.getPixelPosition(object)[1]));
			}catch (AssertionError exc) {
				world.removeObject(object);
				assert(false);
			}
			((Organism) object).setPosition(((Organism) object).getPosition());
			if (object instanceof Organism) {
				((Organism) object).setPosition(((Organism) object).getPosition());
		}
		}catch (AssertionError|ModelException exc) {
			throw new ModelException("invalid Object");
		}
		
	}

	@Override
	public void removeGameObject(Object object, World world) throws ModelException {
		try {
			assert(object != null);
			assert(world != null);
			assert(world.hasAsGameObject(object));
			((Organism) object).removeFromWorld();
		}catch (AssertionError exc) {
			throw new ModelException("...");
		}
	}

	@Override
	public int[] getTargetTileCoordinate(World world) throws ModelException {
		return world.getReturnTargetTile();
	}

	@Override
	public void setTargetTileCoordinate(World world, int[] tileCoordinate) throws ModelException {
		world.setTargetTileCoord(tileCoordinate);
		world.setReturnTargetTile(tileCoordinate);
	}

	@Override
	public void startGame(World world) throws ModelException {
		world.startGame();
	}

	@Override
	public boolean isGameOver(World world) throws ModelException {
		return (world.isGameOver() || world.isGameWon());
	}

	@Override
	public boolean didPlayerWin(World world) throws ModelException {
		return world.isGameWon();
	}

	@Override
	public void advanceWorldTime(World world, double dt) {
		if (dt != Double.NaN) {
			if (dt >= 0 && dt <= 0.2) {
				world.advanceTime(dt);
			}
			else {
				throw new ModelException("invalid time");
			}
		}
		else {
			throw new ModelException("invalid time");
		}
		
	}

	@Override
	public void terminateGameObject(Object gameObject) throws ModelException {
		Organism obj = (Organism) gameObject;
		obj.terminate();
	}

	@Override
	public boolean isTerminatedGameObject(Object gameObject) throws ModelException {
		Organism obj = (Organism) gameObject;
		return ((obj.isEliminated() && obj.getTimeDeath() >= 0.6)|| obj.isTerminated());
	}

	@Override
	public boolean isDeadGameObject(Object gameObject) throws ModelException {
		Organism obj = (Organism) gameObject;
		return !obj.isAlive();
	}

	@Override
	public double[] getActualPosition(Object gameObject) throws ModelException {
		Organism obj = (Organism) gameObject;
		return obj.getMeterPosition();
	}

	@Override
	public void changeActualPosition(Object gameObject, double[] newPosition) throws ModelException{
		if(Double.isNaN(newPosition[0]) || Double.isNaN(newPosition[1])) {
			throw new ModelException("position can't be NaN");
		}
		Organism obj = (Organism) gameObject;
		obj.setMeterPosition(newPosition);
		
		
	}

	@Override
	public int[] getPixelPosition(Object gameObject) throws ModelException {
		Organism obj = (Organism) gameObject;
		return obj.getPosition();
	}

	@Override
	public int getOrientation(Object gameObject) throws ModelException {
		Organism obj = (Organism) gameObject;
		return obj.getDirection().dirToInt();
	}

	@Override
	public double[] getVelocity(Object gameObject) throws ModelException {
		Organism obj = (Organism) gameObject;
		return obj.getVelocity();
	}

	@Override
	public World getWorld(Object object) throws ModelException {
		if (object instanceof Organism) {
			return ((Organism) object).getWorld();
		}
		else if (object instanceof School) {
			return ((School) object).getWorld();
		}
		else {
			return null;
		}
	}

	@Override
	public int getHitPoints(Object object) throws ModelException {
		Organism obj = (Organism) object;
		return obj.getHP();
	}

	@Override
	public Sprite[] getSprites(Object gameObject) throws ModelException {
		Organism obj = (Organism) gameObject;
		return obj.getSprites();
	}

	@Override
	public void advanceTime(Object gameObject, double dt) throws ModelException {
		// TODO Auto-generated method stub
		if (dt >=0) {
			Organism obj = (Organism) gameObject;
			obj.advanceTime(dt);
		}
		else {
			throw new ModelException("invalid time");
		}
	}
	
	public int[] getVisibleWindowPosition(World world) {
		return world.getLowerLeftPixelWindow();
	}
	
	public Sprite getCurrentSprite(Object gameObject) {
		Organism obj = (Organism) gameObject;
		return obj.getCurrentSprite();
	}

	@Override
	public boolean isLateTeamSplit() {
		return false;
	}

	@Override
	public Set<School> getAllSchools(World world) throws ModelException {
		return world.getSchools();
	}

	@Override
	public double[] getAcceleration(Object gameObject) throws ModelException {
		return ((Organism) gameObject).getAcceleration();
	}

	@Override
	public Sneezewort createSneezewort(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
		Sneezewort sneeze = new Sneezewort(pixelLeftX, pixelBottomY, sprites);
		return sneeze;
	}

	@Override
	public Skullcab createSkullcab(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
		Skullcab skull = new Skullcab(pixelLeftX, pixelBottomY, sprites);
		return skull;
	}

	@Override
	public Slime createSlime(long id, int pixelLeftX, int pixelBottomY, School school, Sprite... sprites)
			throws ModelException {
		if (Slime.getIdList().contains(id)) {
			throw new ModelException("Id already used");
		}
		else if (id < 0) {
			throw new ModelException("invalid ID");
		}
		Slime slim = new Slime(id, pixelLeftX, pixelBottomY, school, sprites);
		return slim;
	}

	@Override
	public long getIdentification(Slime slime) throws ModelException {
		return slime.getId();
	}

	@Override
	public void cleanAllSlimeIds() {
		Slime.clearIds();
	}

	@Override
	public School createSchool(World world) throws ModelException {
		try {
			if(world != null) {
				assert(world.getSchools().size() < world.getMaxNbOfSchools());
			}
		}catch (AssertionError exc) {
			throw new ModelException("To0 many schools");
		}
		School school = new School<>(world);
		return school;
	}

	@Override
	public void terminateSchool(School school) throws ModelException {
		school.terminate();
	}

	@Override
	public boolean hasAsSlime(School school, Slime slime) throws ModelException {
		boolean ret = false;
		if (school.getStudents().contains((Organism) slime)) {
			ret = true;
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends Slime> getAllSlimes(School school) {
		return school.getStudents();
	}

	@Override
	public void addAsSlime(School school, Slime slime) throws ModelException {
		if (slime != null && school != null) {
			if (slime.isAlive() && school.isAlive()) {
				if (slime.getSchool() == null) {
					slime.setSchool(school);
					school.addStudent(slime);
				}
				else {
					throw new ModelException("already in school");
				}
			}
			else {
				throw new ModelException("student or school is dead");
			}
		}
		else {
			throw new ModelException("slime or school is null");
		}
	}

	@Override
	public void removeAsSlime(School school, Slime slime) throws ModelException {
		try {
			assert(school.getStudents().contains(slime));
			slime.leaveSchool();
			school.removeStudent(slime);
		}catch (AssertionError exc) {
			throw new ModelException("...");
		}
	}

	@Override
	public void switchSchool(School newSchool, Slime slime) throws ModelException {
		if (newSchool == null) {
			throw new ModelException("no new school");
		}
		else if (!newSchool.isAlive()) {
			throw new ModelException("new school is dead");
		}
		School old = slime.getSchool();
		slime.switchSchool(newSchool);
		if (old != null) {
			old.removeStudent(slime);
		}
		newSchool.addStudent(slime);
	}

	@Override
	public School getSchool(Slime slime) throws ModelException {
		return slime.getSchool();
	}

	@Override
	public boolean hasImplementedWorldWindow() {
		return false;
	}
	
	@Override
	public Shark createShark(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
		return new Shark(pixelLeftX, pixelBottomY, sprites);
	}

}
