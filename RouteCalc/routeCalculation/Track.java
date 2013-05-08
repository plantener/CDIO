package routeCalculation;

import java.util.ArrayList;

import models.BreakPoint;
import models.ObjectOnMap;
import models.Port;

public class Track implements Runnable {

	private ObjectOnMap[] objects;

	private Route[] r = new Route[6];

	private static ArrayList<BreakPoint> complete = new ArrayList<BreakPoint>();
	
	public Track(ObjectOnMap[] objects) {
		this.objects = objects;
	}

	public static void addList(ArrayList<BreakPoint> newBreak, ArrayList<BreakPoint> oldBreak){
		int index;
		if(!oldBreak.isEmpty()){
			index = complete.indexOf(oldBreak.get(0));
			complete.removeAll(oldBreak);
			complete.addAll(index, newBreak);
		}
		else{
			complete.addAll(newBreak);
		}
	}
	
	public static ArrayList<BreakPoint> getCompleteList(){
		return complete;
	}
	
	private void init() {
		r[0] = new Route((Port) objects[14], (Port) objects[15]);
		r[1] = new Route((Port) objects[15], (Port) objects[16]);
		r[2] = new Route((Port) objects[16], (Port) objects[17]);
		r[3] = new Route((Port) objects[17], (Port) objects[18]);
		r[4] = new Route((Port) objects[18], (Port) objects[19]);
		r[5] = new Route((Port) objects[19], (Port) objects[14]);
	}
	
	public void updateobjects(){
		
	}

	@Override
	public void run() {
		init();
		for (Route start : r) {
			start.find();
		}
		while (true) {
			if (objects[14] != r[0].getStart()) {
				r[0].update((Port) objects[14], (Port) objects[15]);
				r[5].update((Port) objects[19], (Port) objects[14]);
				r[0].find();
				r[5].find();
			} else if (objects[15] != r[1].getStart()) {
				r[0].update((Port) objects[14], (Port) objects[15]);
				r[1].update((Port) objects[15], (Port) objects[16]);
				r[0].find();
				r[1].find();
			} else if (objects[16] != r[2].getStart()) {
				r[1].update((Port) objects[15], (Port) objects[16]);
				r[2].update((Port) objects[17], (Port) objects[18]);
				r[1].find();
				r[2].find();
			} else if (objects[17] != r[3].getStart()) {
				r[2].update((Port) objects[16], (Port) objects[17]);
				r[3].update((Port) objects[17], (Port) objects[18]);
				r[2].find();
				r[3].find();
			} else if (objects[18] != r[4].getStart()) {
				r[3].update((Port) objects[17], (Port) objects[18]);
				r[4].update((Port) objects[18], (Port) objects[19]);
				r[3].find();
				r[4].find();
			} else if (objects[19] != r[5].getStart()) {
				r[4].update((Port) objects[18], (Port) objects[19]);
				r[5].update((Port) objects[19], (Port) objects[14]);
				r[4].find();
				r[5].find();
			}
		}

	}

}
