package process;

import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

/**
 * This class is the description of any Attributes corresponding at a Process (node)
 * @autor Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 */
@Data
@SuppressWarnings("all")
public class Attribute {
  /**
   * The Integer type of the attribute
   * We have to keep it to optimize the value update
   */
  private final UnsignedInteger typeOPCAttributes;
  
  /**
   * The value of the attribute
   */
  private final Object value;
  
  public Attribute(final UnsignedInteger typeOPCAttributes, final Object value) {
    super();
    this.typeOPCAttributes = typeOPCAttributes;
    this.value = value;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.typeOPCAttributes== null) ? 0 : this.typeOPCAttributes.hashCode());
    result = prime * result + ((this.value== null) ? 0 : this.value.hashCode());
    return result;
  }
  
  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Attribute other = (Attribute) obj;
    if (this.typeOPCAttributes == null) {
      if (other.typeOPCAttributes != null)
        return false;
    } else if (!this.typeOPCAttributes.equals(other.typeOPCAttributes))
      return false;
    if (this.value == null) {
      if (other.value != null)
        return false;
    } else if (!this.value.equals(other.value))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("typeOPCAttributes", this.typeOPCAttributes);
    b.add("value", this.value);
    return b.toString();
  }
  
  @Pure
  public UnsignedInteger getTypeOPCAttributes() {
    return this.typeOPCAttributes;
  }
  
  @Pure
  public Object getValue() {
    return this.value;
  }
}
