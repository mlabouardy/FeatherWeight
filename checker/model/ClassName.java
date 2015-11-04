package checker.model;

public final class ClassName {
	String className;
	
	public ClassName(String className) {
		this.className = className.intern();
	}
	
	public String getClassName() {
		return this.className;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) { return true; }
		
		if (obj instanceof ClassName) {
			ClassName cn2 = (ClassName) obj;
			return cn2.className == className;
		};
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return className.hashCode();
	}
	
	public String toString() {
		return "ClassName: " + this.className;
	}
}
