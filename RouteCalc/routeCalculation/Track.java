package routeCalculation;

import java.util.ArrayList;


import Objects.Cord;

public class Track implements Runnable {

	private Cord[] ports;;

	private Route[] r = new Route[6];

	public Track(Cord[] ports) {
		this.ports = ports;
	}

	private void init() {
		r[0] = new Route(ports[0], ports[1]);
		r[1] = new Route(ports[1], ports[2]);
		r[2] = new Route(ports[2], ports[3]);
		r[3] = new Route(ports[3], ports[4]);
		r[4] = new Route(ports[4], ports[5]);
		r[5] = new Route(ports[5], ports[0]);
	}

	@Override
	public void run() {
		init();
		for (Route start : r) {
			new Thread(start).start();
		}
		while (true) {
			if (ports[0] != r[0].getStart()) {
				r[0].update(ports[0], ports[1]);
				r[5].update(ports[5], ports[0]);
			} else if (ports[1] != r[1].getStart()) {
				r[0].update(ports[0], ports[1]);
				r[1].update(ports[1], ports[2]);
			} else if (ports[2] != r[2].getStart()) {
				r[1].update(ports[1], ports[2]);
				r[2].update(ports[2], ports[3]);
			} else if (ports[3] != r[3].getStart()) {
				r[2].update(ports[2], ports[3]);
				r[3].update(ports[3], ports[4]);
			} else if (ports[4] != r[4].getStart()) {
				r[3].update(ports[3], ports[4]);
				r[4].update(ports[4], ports[5]);
			} else if (ports[5] != r[5].getStart()) {
				r[4].update(ports[4], ports[5]);
				r[5].update(ports[5], ports[0]);
			}
		}

	}

}
