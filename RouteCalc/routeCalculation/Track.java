package routeCalculation;

import java.util.ArrayList;

import objectHandling.DeadSpaceCalculation;

import models.BreakPoint;
import models.ObjectOnMap;
import models.Port;
import models.Box;

public class Track {

	private ObjectOnMap[] objects;

	private Route[] r = new Route[6];

	private static ArrayList<BreakPoint> complete = new ArrayList<BreakPoint>();

	private final int OFFSET = 8;
	
	public Track(ObjectOnMap[] objects) {
		this.objects = objects;

		init();
		for (Route start : r) {
			start.find();
		}
	}

	public static void addList(ArrayList<BreakPoint> newBreak,
			ArrayList<BreakPoint> oldBreak) {
		int index;
		if (!oldBreak.isEmpty()) {
			index = complete.indexOf(oldBreak.get(0));
			complete.removeAll(oldBreak);
			complete.addAll(index, newBreak);
		} else {
			complete.addAll(newBreak);
		}
	}

	public static ArrayList<BreakPoint> getCompleteList() {
		return complete;
	}

	private void init() {
		r[0] = new Route((Port) objects[14], (Port) objects[15]);
		r[1] = new Route((Port) objects[15], (Port) objects[16]);
		r[2] = new Route((Port) objects[16], (Port) objects[17]);
		r[3] = new Route((Port) objects[17], (Port) objects[18]);
		r[4] = new Route((Port) objects[18], (Port) objects[19]);
		r[5] = new Route((Port) objects[19], (Port) objects[14]);

		for (int i = 0; i < 12; i++) {
			Box temp = (Box) objects[i];
			temp.setX(temp.getX() - OFFSET);
			temp.setY(temp.getY() - OFFSET);
			temp.setHeight(temp.getHeight() + (OFFSET*2));
			temp.setWidth(temp.getWidth() + (OFFSET*2));
			DeadSpaceCalculation.addBox(temp);
		}
	}

	public void updateObjects(ObjectOnMap[] objects) {
		updateNewPort(objects);
		updateBoxes(objects);
		this.objects = objects;
	}

	public void updateNewPort(ObjectOnMap[] newObjects) {
		for (int i = 14; i < 20; i++) {
			if (((Port) objects[i]).getMidX() != r[i - 14].getStart().getMidX()
					|| ((Port) objects[i]).getMidY() != r[i - 14].getStart()
							.getMidY()) {
				if (i == 14) {
					r[i - 14].update((Port) objects[i], (Port) objects[i + 1]);
					r[5].update((Port) objects[19], (Port) objects[i]);
					r[5].find();
					r[i - 14].find();
				} else {
					r[i - 15].update((Port) objects[i - 1], (Port) objects[i]);
					if (i == 19) {
						r[i - 14].update((Port) objects[i], (Port) objects[14]);
					} else {
						r[i - 14].update((Port) objects[i],
								(Port) objects[i + 1]);
					}
					r[i - 15].find();
					r[i - 14].find();
				}
			}
		}
	}

	public void updateBoxes(ObjectOnMap[] newObjects) {
		for (int i = 0; i < 12; i++) {
			if (objects[i].getMidX() != newObjects[i].getMidX()
					&& objects[i].getMidY() != newObjects[i].getMidY()) {
				DeadSpaceCalculation.replaceBox((Box) newObjects[i],
						(Box) objects[i]);
				for (Route route : r) {
					route.find();
				}
			}
		}
	}
}
