package tools

import org.opcfoundation.ua.builtintypes.UnsignedInteger
import org.opcfoundation.ua.core.Attributes

/**
 * This class is used for all translation between the number of an Attributes and his String representation
 * 
 * @author Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 */
class TranslateAttributeId {
	
	/**
	 * @param typeOPCAttributes The number corresponding to the attributes following OPC UA Standard
	 * @return A String user's readable 
	 */
	def static getName(UnsignedInteger typeOPCAttributes) {
		if (typeOPCAttributes == Attributes::AccessLevel)
			"AccessLevel"
		else if (typeOPCAttributes == Attributes::ArrayDimensions)
			"ArrayDimensions"
		else if (typeOPCAttributes == Attributes::BrowseName)
			"BrowseName"
		else if (typeOPCAttributes == Attributes::ContainsNoLoops)
			"ContainsNoLoops"
		else if (typeOPCAttributes == Attributes::DataType)
			"DataType"
		else if (typeOPCAttributes == Attributes::Description)
			"Description"
		else if (typeOPCAttributes == Attributes::DisplayName)
			"DisplayName"
		else if (typeOPCAttributes == Attributes::EventNotifier)
			"EventNotifier"
		else if (typeOPCAttributes == Attributes::Executable)
			"Executable"
		else if (typeOPCAttributes == Attributes::Historizing)
			"Historizing"
		else if (typeOPCAttributes == Attributes::InverseName)
			"InverseName"
		else if (typeOPCAttributes == Attributes::IsAbstract)
			"IsAbstract"
		else if (typeOPCAttributes == Attributes::MinimumSamplingInterval)
			"MinimumSamplingInterval"
		else if (typeOPCAttributes == Attributes::NodeClass)
			"NodeClass"
		else if (typeOPCAttributes == Attributes::NodeId)
			"NodeId"
		else if (typeOPCAttributes == Attributes::Symmetric)
			"Symmetric"
		else if (typeOPCAttributes == Attributes::UserAccessLevel)
			"UserAccessLevel"
		else if (typeOPCAttributes == Attributes::UserExecutable)
			"UserExecutable"
		else if (typeOPCAttributes == Attributes::UserWriteMask)
			"UserWriteMask"
		else if (typeOPCAttributes == Attributes::Value)
			"Value"
		else if (typeOPCAttributes == Attributes::ValueRank)
			"ValueRank"
		else if (typeOPCAttributes == Attributes::WriteMask)
			"WriteMask"
		else
			"ERROR"
	}
	
	/**
	 * @param string The string corresponding to Attribute
	 * @see #getName(UnsignedInteger typeOPCAttributes)
	 * @return The number corresponding to the string
	 */
	def static getAttributeNumber(String string) {
		if (string === "AccessLevel") {
			Attributes::AccessLevel
		} else if (string === "ArrayDimensions") {
			Attributes::ArrayDimensions
		} else if (string === "BrowseName") {
			Attributes::BrowseName
		} else if (string === "ContainsNoLoops") {
			Attributes::ContainsNoLoops
		} else if (string === "DataType") {
			Attributes::DataType
		} else if (string === "Description") {
			Attributes::Description
		} else if (string === "DisplayName") {
			Attributes::DisplayName
		} else if (string === "EventNotifier") {
			Attributes::EventNotifier
		} else if (string === "Executable") {
			Attributes::Executable
		} else if (string === "Historizing") {
			Attributes::Historizing
		} else if (string === "InverseName") {
			Attributes::InverseName
		} else if (string === "IsAbstract") {
			Attributes::IsAbstract
		} else if (string === "MinimumSamplingInterval") {
			Attributes::MinimumSamplingInterval
		} else if (string === "NodeClass") {
			Attributes::NodeClass
		} else if (string === "NodeId") {
			Attributes::NodeId
		} else if (string === "Symmetric") {
			Attributes::Symmetric
		} else if (string === "UserAccessLevel") {
			Attributes::UserAccessLevel
		} else if (string === "UserExecutable") {
			Attributes::UserExecutable
		} else if (string === "UserWriteMask") {
			Attributes::UserWriteMask
		} else if (string === "Value") {
			Attributes::Value
		} else if (string === "ValueRank") {
			Attributes::ValueRank
		} else {
			Attributes::WriteMask
		}
	}
	
}