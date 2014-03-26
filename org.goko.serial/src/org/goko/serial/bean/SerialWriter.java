package org.goko.serial.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Runnable to send data to Serial
 *
 * @author PsyKo
 *
 */
public class SerialWriter implements Runnable {
	/** Output stream */
	private OutputStream output;
	/** Data buffer */
	//private List<List<Byte>> buffer;
	private List<Byte> buffer;
    /** Stop status */
    private boolean stopped;
    /** Xon/Xoff flow control activated */
    private boolean flowControlEnabled;
    /** Flow control state */
    private boolean clearToSend;
    /** Lock to prevent reading and sending at the same time */
    private Lock readerWriterLock;
    private Lock bufferLock;

    /**
     * Constructor
     * @param out
     * @param lock
     */
    public SerialWriter ( OutputStream out, Lock lock) {
    	buffer = Collections.synchronizedList(new ArrayList<Byte>());
        this.output = out;
        this.readerWriterLock = lock;
        bufferLock = new ReentrantLock();
        this.flowControlEnabled = true;
        this.clearToSend = true;

    }
    /**
     * Add a command to the queue
     * @param command
     * @return an internal Id for the sent data, for aknowledgement
     */
    public void addCommandToHeadOfQueue(List<Byte> command){
    	try {
    		bufferLock.lock();
    		getBuffer().addAll(0, command);
    		//simpleBuffer.addAll(0, command);
    	}finally{
    		bufferLock.unlock();
    	}
    }

    /**
     * Add a command to the queue
     * @param command
     * @return an internal Id for the sent data, for aknowledgement
     */
    public void addCommandToTailOfQueue(List<Byte> command){
    	try {
    		bufferLock.lock();
    		getBuffer().addAll(command);
    		//simpleBuffer.addAll(command);
    	}finally{
    		bufferLock.unlock();
    	}
    }

    private OutputStream getOutput(){
    	return output;
    }
    /**
     * Run
     */
    @Override
	public void run ()
    {
//        try{
//        	while(!stopped){
//        		if(!flowControlEnabled || flowControlEnabled && isClearToSend()){
//        			//synchronized(buffer){
//        				if(getBuffer().size() > 0){
//		        			try{
//		        				//readerWriterLock.lock();
//		        				bufferLock.lock();
//		        				List<Byte> data;
//
//		        				data = new ArrayList<Byte>(getBuffer().remove(0));
//
//						        for(Byte b: data){
//						        	getOutput().write( b );
//						        	//getOutput().write( ArrayUtils.toPrimitive(data.toArray(new Byte[]{})) );
//						        }
//
//		        			}finally{
//		        				bufferLock.unlock();
//				            	//readerWriterLock.unlock();
//				            }
//
//        				}
//
//        			//}
//        		}
//        	}
//        }
//        catch ( IOException e )
//        {
//            e.printStackTrace();
//        }

        try{
        	while(!stopped){
				while(getBuffer().size() > 0 && isClearToSend()){
        			try{
        				bufferLock.lock();
				        getOutput().write( getBuffer().remove(0) );

        			}finally{
        				bufferLock.unlock();

		            }
				}
        	}
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
	/**
	 * @return the clearToSend
	 */
	public boolean isClearToSend() {
		return clearToSend;
	}
	/**
	 * @param clearToSend the clearToSend to set
	 */
	public void setClearToSend(boolean clearToSend) {
		this.clearToSend = clearToSend;
	}
	/**
	 * @return the buffer
	 */
	public List<Byte> getBuffer() {
		return buffer;
	}

	public void clearOutputBuffer() {
		try {
    		bufferLock.lock();
    		getBuffer().clear();
    	}finally{
    		bufferLock.unlock();
    	}
	}

}


