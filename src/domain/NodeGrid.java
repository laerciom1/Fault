package domain;

import java.util.ArrayList;

public class NodeGrid {
	private int id;
	private ArrayList<Integer> app;
	private boolean working;
	private double realiability;
	
	public NodeGrid(int id){
		this.id = id;
		this.app = new ArrayList<Integer>();
		this.working = true;
		this.realiability = 1;
	}
	
	public NodeGrid(int id, boolean working, double reliability){
		this.id = id;
		this.app = new ArrayList<Integer>();
		this.working = working;
		this.realiability = reliability;
	}
	
	ArrayList<Integer> getApps(){
		return app;
	}
	
	public void addApp(int app) {
		if(this.app.size() == 1){
			if(this.app.get(0) == -1){
				this.app.clear();
				this.app.add(app);
			}
			else{
				this.app.add(app);
			}
		}
		else{
			this.app.add(app);
		}
	}
	
	public void clear(){
		this.app.clear();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isWorking() {
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
