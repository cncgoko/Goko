package org.goko.viewer.jogl.utils.render;

import javax.media.opengl.GL2;
import javax.vecmath.Point3d;

import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.log.GkLog;
import org.goko.viewer.jogl.service.JoglRendererProxy;

public class ToolRenderer implements IJoglRenderer {
	protected static final String ID = "org.goko.viewer.jogl.utils.render.ToolRenderer";
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ToolRenderer.class);
	/** The position of the tool*/
	private Point3d toolPosition;
	/** the controller service used to retrieve the tool position */
	private IControllerService controllerService;

	/**
	 * Constructor
	 * @param controllerService the controller service used to retrieve the tool position
	 */
	public ToolRenderer(IControllerService controllerService) {
		this.controllerService = controllerService;
		if(controllerService != null){
			this.controllerService.addListener(this);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.IJoglRenderer#render(org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void render(JoglRendererProxy proxy) throws GkException {
		GL2 gl = proxy.getGl();
		if (toolPosition == null) {
			toolPosition = new Point3d(0,0,0);
		}

		// Draw the path to the tool
		gl.glEnable(GL2.GL_LINE_STIPPLE);
		gl.glLineStipple(1, (short) 0x00FF);
		gl.glLineWidth(2f);
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3f(1, 0, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(toolPosition.x, 0, 0);
		gl.glColor3f(0, 1, 0);
		gl.glVertex3d(toolPosition.x, 0, 0);
		gl.glVertex3d(toolPosition.x, toolPosition.y, 0);
		gl.glColor3f(0, 0, 1);
		gl.glVertex3d(toolPosition.x, toolPosition.y, 0);
		gl.glVertex3d(toolPosition.x, toolPosition.y, toolPosition.z);
		gl.glEnd();
		gl.glDisable(GL2.GL_LINE_STIPPLE);
		// Draw the tool
		gl.glPushMatrix();
		gl.glTranslated(toolPosition.x, toolPosition.y, toolPosition.z);

		gl.glLineWidth(3.0f);
		gl.glBegin(GL2.GL_LINE_STRIP);
		gl.glColor4f(0.87f, 0.31f, 0.43f, 1f);
		int segmentCount = 16;
		int radius = 3 / 2;
		float angle = (float) ((Math.PI * 2) / segmentCount);
		for (int i = 0; i <= segmentCount; i++) {
			double x = radius * Math.cos(i * angle);
			double y = radius * Math.sin(i * angle);
			gl.glVertex3d(x, y, 6);
		}
		gl.glEnd();
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3d(radius, 0, 6);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(-radius, 0, 6);
		gl.glVertex3d(0, radius, 6);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, -radius, 6);
		gl.glEnd();
		gl.glPopMatrix();

	}

	/**
	 * Event listener on the Controller service
	 * @param event the update event
	 */
	@EventListener(MachineValueUpdateEvent.class)
	public void onMachineValueUpdateEvent(MachineValueUpdateEvent event){
		try {
			setToolPosition( controllerService.getPosition() );
		} catch (GkException e) {
			LOG.error(e);
		}
	}
	/**
	 * @return the toolPosition
	 */
	public Point3d getToolPosition() {
		return toolPosition;
	}

	/**
	 * @param toolPosition the toolPosition to set
	 */
	public void setToolPosition(Point3d toolPosition) {
		this.toolPosition = toolPosition;
	}

}
