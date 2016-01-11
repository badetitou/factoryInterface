package process

import org.eclipse.xtend.lib.annotations.Data
import org.opcfoundation.ua.builtintypes.UnsignedInteger

/**
 * This class is the description of any Attributes corresponding at a Process (node)
 * @autor Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 */
@Data class Attribute {
	
	/**
	 * The Integer type of the attribute
	 * We have to keep it to optimize the value update
	 */
	UnsignedInteger typeOPCAttributes
	
	/**
	 * The value of the attribute
	 */
	Object value
}