package routeCalculation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import objectHandling.DeadSpaceCalculation;

import models.BreakPoint;
import models.ObjectOnMap;
import models.Port;
import models.Box;

public class Track {

	private ArrayList<ObjectOnMap> ports = new ArrayList<ObjectOnMap>();
	private ArrayList<Box> boxes = new ArrayList<Box>();
	
	private static ArrayList<Route> r = new ArrayList<Route>();

	private static ArrayList<BreakPoint> complete = new ArrayList<BreakPoint>();

	private final int OFFSET = 10;
	
	private final int UPDATE_OFFSET = 0;
	
	public Track(ArrayList<ObjectOnMap> ports, ArrayList<Box> red, ArrayList<Box> green) {
		this.ports = ports;
		this.boxes.addAll(red);
		this.boxes.addAll(green);
		System.out.println(red.size() + " " + green.size() + " " + boxes.size());
		initRoute();
		initBoxes();
		for (Route start : r) {
			start.find();
		}
	}

	public static void addList(ArrayList<BreakPoint> newBreak,
			ArrayList<BreakPoint> oldBreak) {
		int index = 0;
		complete.clear();
		for (Route set : r) {
			complete.addAll(set.getBreakPoints());
		}
		
//		
//		if (!oldBreak.isEmpty()) {
//			index = complete.indexOf(oldBreak.get(0));
//			System.out.println("size:" + oldBreak.size());
//			complete.removeAll(oldBreak);
//			complete.addAll(index, newBreak);
//		} else {
//			complete.addAll(newBreak);
//		}
	}
	
	private static void removePort(int port){
		int index = 0;
		System.out.print(port + ": ");
		for (int i = 0; i < complete.size(); i++) {
			System.out.print(complete.get(i));
			if(port == complete.get(index).getPort()){
				complete.remove(index);
			}else
				index++;
		}
		System.out.println();
	}

	public static ArrayList<BreakPoint> getCompleteList() {
		return complete;
	}

	private void initRoute() {
		r.clear();
		complete.clear();
		int i = 0;
		System.out.println(ports.size());
		for (ObjectOnMap port : ports) {
			if(port != ports.get(ports.size()-1)){
				r.add(new Route((Port) port, (Port) ports.get(i+1)));
			}else{
				r.add(new Route((Port) port, (Port) ports.get(0)));
			}
			i++;
		}
	}
	
	private void initBoxes(){
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
		if(ports.size() != this.r.size()){
			initRoute();
		}
		else{
			updateNewPort(ports);
		}
		if(boxes.size() != (red.size()+green.size())){
			boxes.clear();
			this.boxes.addAll(red);
			this.boxes.addAll(green);
			initBoxes();
		}else
			updateBoxes(red, green);
	}

	public void updateNewPort(ArrayList<ObjectOnMap> newObjects) {
		int size = newObjects.size()-1;
		for (int i = 0; i < newObjects.size(); i++) {
			if (!(((Port) newObjects.get(i)).getMidX() >= r.get(i).getStart().getMidX()-UPDATE_OFFSET && ((Port) newObjects.get(i)).getMidX() <= r.get(i).getStart().getMidX()+UPDATE_OFFSET)
					||!(((Port) newObjects.get(i)).getMidY() >= r.get(i).getStart().getMidY()-UPDATE_OFFSET && ((Port) newObjects.get(i)).getMidY() <= r.get(i).getStart().getMidY()+UPDATE_OFFSET)
					) {
				System.out.println("updates" + i);
				if (i == 0) {
					r.get(i).update((Port) newObjects.get(i), (Port) newObjects.get(i + 1));
					r.get(size).update((Port) newObjects.get(size), (Port) newObjects.get(i));
					r.get(size).find();
					r.get(i).find();
					System.out.println("updates" + i);
				} else {
					r.get(i - 1).update((Port) newObjects.get(i - 1), (Port) newObjects.get(i));
					if (i == size) {
						r.get(i).update((Port) newObjects.get(i), (Port) newObjects.get(0));
					} else {
						r.get(i).update((Port) newObjects.get(i),
								(Port) newObjects.get(i + 1));
					}
					System.out.println("updates" + i);
					r.get(i - 1).find();
					r.get(i).find();
				}
			}
		}
		ports = newObjects;
	}

	public void updateBoxes(ArrayList<Box> red, ArrayList<Box> green) {
		ArrayList<Box> newBoxes = new ArrayList<Box>();
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
			}
		}
		boxes.clear();
		boxes.addAll(red);
		boxes.addAll(green);
	} 
}
