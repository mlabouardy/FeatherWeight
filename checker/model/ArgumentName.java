package checker.model;

public final class ArgumentName {
	
    private final String argName;
		
    public ArgumentName(String argName) {
	if(argName == null) {
	    throw new IllegalArgumentException("argName cannot be null!");
	}
	this.argName = argName.intern();
    }
		
    public String getArgName() {
	return this.argName;
    }
		
    @Override
    public boolean equals(Object other) {
	if(this == other){
	    return true;
	}
			
	if(other instanceof ArgumentName) {
	    ArgumentName othr = (ArgumentName) other;
	    return othr.argName.equals(this.argName);
	}
	return false;
    }

    @Override
    public int hashCode() {
	return argName.hashCode();
    }
}
