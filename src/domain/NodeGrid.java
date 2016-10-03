package domain;

public class NodeGrid {
	private int id;
	private int app;
	private boolean working;
	private double realiability;
	
	public NodeGrid(int id, int app){
		this.id = id;
		this.app = app;
		this.working = true;
		this.realiability = 1;
	}
	
	public NodeGrid(int id, int app, boolean working, double reliability){
		this.id = id;
		this.app = app;
		this.working = working;
		this.realiability = reliability;
	}
	
	int getApp(){
		return app;
	}
	
	public void setApp(int app) {
		this.app = app;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getWorking() {
		return working;
	}

	public void setWorking(boolean working) {
		this.working = working;
	}

	public double getRealiability() {
		return realiability;
	}

	public void setRealiability(double realiability) {
		this.realiability = realiability;
	}
}
