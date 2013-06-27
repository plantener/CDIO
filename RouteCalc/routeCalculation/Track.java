package routeCalculation;

import java.util.ArrayList;

import objectHandling.DeadSpaceCalculation;

import models.BreakPoint;
import models.Port;
import models.Box;

public class Track {

	private ArrayList<Port> ports = new ArrayList<Port>();
	private ArrayList<Box> boxes = new ArrayList<Box>();

	private static ArrayList<Route> r = new ArrayList<Route>();

	private static ArrayList<BreakPoint> complete = new ArrayList<BreakPoint>();
	
	public static boolean newRoute = false;

	private final int WIDTH_OFFSET = 14;
	private final int HEIGHT_OFFSET = 8;

	private final int UPDATE_OFFSET = 5;

	public Track(ArrayList<Port> ports, ArrayList<Box> red,
			ArrayList<Box> green) {
		this.ports = ports;
		this.boxes.addAll(red);
		this.boxes.addAll(green);
		initRoute();
		initBoxes();
		for (Route start : r) {
			start.find();
		}
	}

	public static void addList(ArrayList<BreakPoint> newBreak,
			ArrayList<BreakPoint> oldBreak) {
		complete.clear();
		for (Route set : r) {
			complete.addAll(set.getBreakPoints());
		}
		complete.add(0, complete.get(complete.size()-1));
		newRoute = true;
		
	}

	public static ArrayList<BreakPoint> getCompleteList() {
		return complete;
	}

	private void initRoute() {
		r.clear();
		complete.clear();
		int i = 0;
		for (Port port : ports) {
			if (port != ports.get(ports.size() - 1)) {
				r.add(new Route(port, ports.get(i + 1)));
			} else {
				r.add(new Route(port, ports.get(0)));
			}
			i++;
		}
	}

	private void initBoxes() {
		DeadSpaceCalculation.removeAll();
		for (Box box : boxes) {
			Box temp;
			if(box.getColor() == 2){
				temp = box;
			}else {
				temp = box;
				temp.setX(temp.getX() - WIDTH_OFFSET);
				temp.setY(temp.getY() - HEIGHT_OFFSET);
				temp.setHeight(temp.getHeight() + (HEIGHT_OFFSET * 2));
				temp.setWidth(temp.getWidth() + (WIDTH_OFFSET * 2));
			}
			DeadSpaceCalculation.addBox(temp);
		}
	}

	public void updateObjects(ArrayList<Port> ports, ArrayList<Box> red,
			ArrayList<Box> green) {
		int check = 0;
		
		if (boxes.size() != (red.size() + green.size())) {
			boxes.clear();
			this.boxes.addAll(red);
			this.boxes.addAll(green);
			initBoxes();
			check = 0;
		} else{
			updateBoxes(red, green);
			check = 0;
		}
		if (ports.size() != r.size()) {
			initRoute();
			check = 0;
		} else {
			updateNewPort(ports);
			check = 1;
		}
		if(check == 0){
			for (Route start : r) {
				start.find();
			}
		}
	}

	public void updateNewPort(ArrayList<Port> newObjects) {
		int size = newObjects.size() - 1;
		for (int i = 0; i < newObjects.size(); i++) {
			if (!((newObjects.get(i)).getMidX() >= r.get(i).getStart()
					.getMidX()
					- UPDATE_OFFSET && (newObjects.get(i)).getMidX() <= r
					.get(i).getStart().getMidX()
					+ UPDATE_OFFSET)
					|| !((newObjects.get(i)).getMidY() >= r.get(i)
							.getStart().getMidY()
							- UPDATE_OFFSET && (newObjects.get(i))
							.getMidY() <= r.get(i).getStart().getMidY()
							+ UPDATE_OFFSET)) {
				if (i == 0) {
					if(size != 0){
						r.get(i).update(newObjects.get(i),
								newObjects.get(i + 1));
						r.get(size).update(newObjects.get(size),
								newObjects.get(i));
						r.get(size).find();
						r.get(i).find();
					}
				} else {
					r.get(i - 1).update(newObjects.get(i - 1),
							newObjects.get(i));
					if (i == size) {
						r.get(i).update(newObjects.get(i),
								newObjects.get(0));
					} else {
						r.get(i).update(newObjects.get(i),
								newObjects.get(i + 1));
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
				Box temp;
				if(boxes.get(i).getColor() == 2){
					temp = newBoxes.get(i);
					temp.setX(temp.getX());
					temp.setY(temp.getY());
					temp.setHeight(temp.getHeight());
					temp.setWidth(temp.getWidth());
				}else {
					temp = newBoxes.get(i);
					temp.setX(temp.getX() - WIDTH_OFFSET);
					temp.setY(temp.getY() - HEIGHT_OFFSET);
					temp.setHeight(temp.getHeight() + (HEIGHT_OFFSET * 2));
					temp.setWidth(temp.getWidth() + (WIDTH_OFFSET * 2));
					
				}
				DeadSpaceCalculation.replaceBox((Box) temp, (Box) boxes.get(i));
			}
		}
		boxes.clear();
		boxes.addAll(red);
		boxes.addAll(green);
	}
}
