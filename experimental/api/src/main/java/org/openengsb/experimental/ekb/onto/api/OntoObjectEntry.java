package org.openengsb.experimental.ekb.onto.api;

public class OntoObjectEntry {
	String subject;
	String predicate;
	String object;
	
	public OntoObjectEntry(String subject, String predicate, String object) {
		super();
		this.subject = subject;
		this.predicate = predicate;
		this.object = object; 
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		sb.append("s=").append(subject);
		sb.append("p=").append(predicate);
		sb.append("o=").append(object);
		sb.append("}");
		
		return sb.toString();
	}
}
