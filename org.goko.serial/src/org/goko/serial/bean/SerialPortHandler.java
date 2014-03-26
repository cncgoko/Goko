package org.goko.serial.bean;

import gnu.io.CommPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.event.EventListenerList;

import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.serial.ISerialDataListener;

/**
 * A serial port handler
 *
 * @author PsyKo
 *
 */
public class SerialPortHandler implements SerialPortEventListener {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(SerialPortHandler.class);

	private CommPort 	commPort;
	private String 		portName;
	private InputStream  inStream;
	private OutputStream outStream;
	private EventListenerList listeners;
	private SerialWriter writer;
	private Lock readerWriterLock;
	private ExecutorService notifierThread;

	/**
	 * Constructor
	 * @param inStream input stream of the serial port
	 * @param outStream output stream of the serial port
	 */
	public SerialPortHandler(String portName, InputStream inStream, OutputStream outStream) {
		super();

		this.portName = portName;
		this.inStream = inStream;
		this.outStream = outStream;

		this.listeners = new EventListenerList();
		this.readerWriterLock = new ReentrantLock();
		this.notifierThread = Executors.newSingleThreadExecutor();
	}


	/**
	 * Handle serial events
	 */
	@Override
	public void serialEvent(SerialPortEvent event) {
		//readerWriterLock.lock();
		try {

			if(inStream != null && event.getEventType() == SerialPortEvent.DATA_AVAILABLE){
				byte[] buffer = new byte[inStream.available()];

				if(inStream.available() <= 0){
					return;
				}
				inStream.read(buffer);

				final byte[] newBuffer = Arrays.copyOf(buffer, buffer.length);
				LOG.info("Received from Serial "+GkUtils.toStringReplaceCRLF(newBuffer));
				notifierThread.execute(new Runnable() {
					@Override
					public void run() {
						try {
							notifySerialDataListeners(newBuffer);
						} catch (GkException e) {
							e.printStackTrace();
						}
					}
				});

			}
		} catch (Exception e) {
			LOG.error(e);
			inStream = null;
		}
	}

	/**
	 * Notify all the listener that Serial data was received
	 * @param data the data as a String
	 * @throws GkException an exception
	 */
	private void notifySerialDataListeners(byte[] data) throws GkException {
		for(ISerialDataListener listener : listeners.getListeners(ISerialDataListener.class)){
			listener.onDataReceived(data);
		}
	}


	/**
	 * @return the portName
	 */
	public String getPortName() {
		return portName;
	}


	/**
	 * @param portName the portName to set
	 */
	public void setPortName(String portName) {
		this.portName = portName;
	}


	/**
	 * @return the inStream
	 */
	public InputStream getInStream() {
		return inStream;
	}


	/**
	 * @param inStream the inStream to set
	 */
	public void setInStream(InputStream inStream) {
		this.inStream = inStream;
	}

	/**
	 * @return the outStream
	 */
	public OutputStream getOutStream() {
		return outStream;
	}

	/**
	 * @param outStream the outStream to set
	 */
	public void setOutStream(OutputStream outStream) {
		this.outStream = outStream;
	}

	/**
	 * Add an ISerialDataListener
	 * @param listener the ISerialDataListener to add
	 */
	public void addSerialDataListener(ISerialDataListener listener) {
		listeners.add(ISerialDataListener.class, listener);
	}
	/**
	 * Clear output buffer
	 */
	public void clearOutputBuffer(){
		writer.clearOutputBuffer();
	}
	/**
	 * Sends data
	 * @param data the data to send
	 */
	public void sendData(List<Byte> data){
		writer.addCommandToTailOfQueue(data);
	}
	/**
	 * Sends data immediately
	 * @param data the data to send
	 */
	public void sendDataImmediatly(List<Byte> data){
		writer.addCommandToHeadOfQueue(data);
	}

	/**
	 * Starts the serial output thread
	 */
	public void start() {
		writer = new SerialWriter(outStream, readerWriterLock);

		Thread threadWriter = new Thread(writer);
		List<Byte> lst = new ArrayList<Byte>();
		lst.add((byte) 0x11);
		writer.addCommandToHeadOfQueue(lst);
		threadWriter.start();
	}


	/**
	 * @return the commPort
	 */
	public CommPort getCommPort() {
		return commPort;
	}


	/**
	 * @param commPort the commPort to set
	 */
	public void setCommPort(CommPort commPort) {
		this.commPort = commPort;
	}

	public void setXOff(){
		System.err.println("----------------------------------- Xoff");
		writer.setClearToSend(false);
	}

	public void setXOn(){
		System.err.println("----------------------------------- Xon");
		writer.setClearToSend(true);
	}
}