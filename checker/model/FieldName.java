package checker.model;

public class FieldName {

	String fieldName;
	
	public FieldName(String className) {
		this.fieldName = className.intern();
	}
	
	public String getFieldName() {
		return this.fieldName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) { return true; }
		
		if (obj instanceof FieldName) {
			FieldName cn2 = (FieldName) obj;
			return cn2.fieldName == fieldName;
		};
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return fieldName.hashCode();
	}
}
