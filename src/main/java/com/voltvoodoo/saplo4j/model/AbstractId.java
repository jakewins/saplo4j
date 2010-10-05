package com.voltvoodoo.saplo4j.model;

import java.io.Serializable;

import org.json.simple.JSONAware;

/**
 * Abstract parent class to Id classes.
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 */
public abstract class AbstractId implements Serializable, Comparable<AbstractId>, JSONAware {
	
	/**
	 * Unique serialization id
	 */
	private static final long serialVersionUID = -7790173863547630607L;
	
	/**
	 * The actual ID
	 */
	private Long id;
	
	//
	// CONSTRUCTORS
	//
	protected AbstractId() { }
	public    AbstractId(Long id) { this.id = id; }
	
	//
	// PUBLIC
	//
	
	public Long get()          { return id;    }
	
	//
	// FANCY OBJECT OVERRIDES
	//
	
	public String toString() { 
		if(id != null) {
			return id.toString();
		} else {
			return "null";
		}
	}
	
	public String toJSONString() {
		return this.toString();
	}
	
	public boolean equals(Object obj) {
		if( obj instanceof AbstractId) {
			return this.id.equals( ((AbstractId)obj).get() );
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return this.id.hashCode();
	}
	
	public int compareTo(AbstractId otherId) {
		return this.id.compareTo( otherId.id );
	}
}