package routeCalculation;

import java.util.ArrayList;

import objectHandling.DeadSpaceCalculation;

import models.BreakPoint;
import models.ObjectOnMap;
import models.Port;
import models.Box;

public class Track {

	private ArrayList<ObjectOnMap> ports = new ArrayList<ObjectOnMap>();
	private ArrayList<Box> boxes = new ArrayList<Box>();
	
	private ArrayList<Route> r = new ArrayList<Route>();

	private static ArrayList<BreakPoint> complete = new ArrayList<BreakPoint>();

	private final int OFFSET = 8;
	
	public Track(ArrayList<ObjectOnMap> ports, ArrayList<Box> red, ArrayList<Box> green) {
		
		this.ports = ports;
		this.boxes.addAll(red);
		this.boxes.addAll(green);
		
		for (ObjectOnMap port : ports) {
			System.out.println(port.toString());
		}
		
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
		int i = 0;
		for (ObjectOnMap port : ports) {
			if(i != ports.size()-1){
				r.add(new Route((Port) port, (Port) ports.get(i+1)));
			}else{
				r.add(new Route((Port) port, (Port) ports.get(0)));
			}
			i++;
		}
		
		for (Box box : boxes) {
			Box temp = box;
			temp.setX(temp.getX() - OFFSET);
			temp.setY(temp.getY() - OFFSET);
			temp.setHeight(temp.getHeight() + (OFFSET*2));
			temp.setWidth(temp.getWidth() + (OFFSET*2));
			DeadSpaceCalculation.addBox(temp);
		}
	}

	public void updateObjects(ArrayList<ObjectOnMap> ports, ArrayList<Box> red, ArrayList<Box> green) {
		
		System.out.println(this.ports.size() + " " + ports.size());
		
		updateNewPort(ports);
		updateBoxes(red, green);
	}

	public void updateNewPort(ArrayList<ObjectOnMap> newObjects) {
		int size = newObjects.size()-1;
		for (int i = 0; i < newObjects.size(); i++) {
			if (((Port) ports.get(i)).getMidX() != r.get(i).getStart().getMidX()
					|| ((Port) ports.get(i)).getMidY() != r.get(i).getStart()
							.getMidY()) {
				if (i == 0) {
					r.get(i).update((Port) ports.get(i), (Port) ports.get(i + 1));
					r.get(5).update((Port) ports.get(5), (Port) ports.get(i));
					r.get(5).find();
					r.get(i).find();
				} else {
					r.get(i - 1).update((Port) ports.get(i - 1), (Port) ports.get(i));
					if (i == 5) {
						r.get(i).update((Port) ports.get(i), (Port) ports.get(0));
					} else {
						r.get(i).update((Port) ports.get(i),
								(Port) ports.get(i + 1));
					}
					r.get(i - 1).find();
					r.get(i).find();
				}
			}
		}
		ports = newObjects;
	}

	public void updateBoxes(ArrayList<Box> red, ArrayList<Box> green) {
		ArrayList<Box> newBoxes = new ArrayList<Box>();;
		newBoxes.addAll(red);
		newBoxes.addAll(green);
		for (int i = 0; i < boxes.size(); i++) {
			if (boxes.get(i).getMidX() != newBoxes.get(i).getMidX()
					&& boxes.get(i).getMidY() != newBoxes.get(i).getMidY()) {
				Box temp = newBoxes.get(i);
				temp.setX(temp.getX() - OFFSET);
				temp.setY(temp.getY() - OFFSET);
				temp.setHeight(temp.getHeight() + (OFFSET*2));
				temp.setWidth(temp.getWidth() + (OFFSET*2));
				
				DeadSpaceCalculation.replaceBox((Box) temp,
						(Box) boxes.get(i));
				for (Route route : r) {
					route.find();
				}
			}
		}
		boxes.clear();
		boxes.addAll(red);
		boxes.addAll(green);
	}
}
