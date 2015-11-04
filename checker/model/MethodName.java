package checker.model;

public class MethodName {

	//Newtype String methName
	private final String methName;

	public MethodName(String methName) {
		if (methName == null) {
			throw new IllegalArgumentException("methName cannot be null!");
		}
		this.methName = methName.intern();
	}

	public String getmethName() {
		return this.methName;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (other instanceof MethodName) {
			MethodName othr = (MethodName) other;
			return othr.methName.equals(this.methName);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return methName.hashCode();
	}
}
