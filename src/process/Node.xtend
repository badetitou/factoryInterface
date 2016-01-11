package process

import com.prosysopc.ua.client.UaClient
import java.util.ArrayList
import org.eclipse.xtend.lib.annotations.Accessors
import org.opcfoundation.ua.builtintypes.NodeId
import org.opcfoundation.ua.core.Attributes
import org.opcfoundation.ua.core.ReadValueId
import org.opcfoundation.ua.core.TimestampsToReturn

/**
 * This class is the representation of a Process.
 * With the possibility to update it attributes
 * @autor Benoît Verhaeghe
 */
class Node {

	/**
	 * The nodeId of the process to have a better access to it values
	 */
	@Accessors(PUBLIC_GETTER)NodeId nodeId
	/**
	 * List of all process's attributes
	 */
	@Accessors(PUBLIC_GETTER)ArrayList<Attribute> attributes
	
	/**
	 * @param the nodeId for this node
	 */
	new(NodeId nodeId) {
		this.nodeId = nodeId
		attributes = new ArrayList<Attribute>
	}
	
	/**
	 * Update all process's attributes's values and put new values to the list
	 * @param client The client associate to the server for this process
	 */
	def updateAttributes(UaClient client) {
		attributes.clear

		var nodesToRead = <ReadValueId>newArrayOfSize(22)
		/* Creation of all request*/ 
		nodesToRead.set(0, new ReadValueId(nodeId, Attributes::AccessLevel, null, null))
		nodesToRead.set(1, new ReadValueId(nodeId, Attributes::ArrayDimensions, null, null))
		nodesToRead.set(2, new ReadValueId(nodeId, Attributes::BrowseName, null, null))
		nodesToRead.set(3, new ReadValueId(nodeId, Attributes::ContainsNoLoops, null, null))
		nodesToRead.set(4, new ReadValueId(nodeId, Attributes::DataType, null, null))
		nodesToRead.set(5, new ReadValueId(nodeId, Attributes::Description, null, null))
		nodesToRead.set(6, new ReadValueId(nodeId, Attributes::DisplayName, null, null))
		nodesToRead.set(7, new ReadValueId(nodeId, Attributes::EventNotifier, null, null))
		nodesToRead.set(8, new ReadValueId(nodeId, Attributes::Executable, null, null))
		nodesToRead.set(9, new ReadValueId(nodeId, Attributes::Historizing, null, null))
		nodesToRead.set(10, new ReadValueId(nodeId, Attributes::InverseName, null, null))
		nodesToRead.set(11, new ReadValueId(nodeId, Attributes::IsAbstract, null, null))
		nodesToRead.set(12, new ReadValueId(nodeId, Attributes::MinimumSamplingInterval, null, null))
		nodesToRead.set(13, new ReadValueId(nodeId, Attributes::NodeClass, null, null))
		nodesToRead.set(14, new ReadValueId(nodeId, Attributes::NodeId, null, null))
		nodesToRead.set(15, new ReadValueId(nodeId, Attributes::Symmetric, null, null))
		nodesToRead.set(16, new ReadValueId(nodeId, Attributes::UserAccessLevel, null, null))
		nodesToRead.set(17, new ReadValueId(nodeId, Attributes::UserExecutable, null, null))
		nodesToRead.set(18, new ReadValueId(nodeId, Attributes::UserWriteMask, null, null))
		nodesToRead.set(19, new ReadValueId(nodeId, Attributes::Value, null, null))
		nodesToRead.set(20, new ReadValueId(nodeId, Attributes::ValueRank, null, null))
		nodesToRead.set(21, new ReadValueId(nodeId, Attributes::WriteMask, null, null))

		/* Read all data */
		var response = client.read(UaClient::MAX_CACHE_AGE, TimestampsToReturn::Both, nodesToRead).results

		/* Test results */
		if (response.get(0).statusCode.good) {
			attributes.add(new Attribute(Attributes::AccessLevel, response.get(0).value.toString))
		}
		if (response.get(1).statusCode.good) {
			attributes.add(new Attribute(Attributes::ArrayDimensions, response.get(1).value.toString))
		}
		if (response.get(2).statusCode.good) {
			attributes.add(new Attribute(Attributes::BrowseName, response.get(2).value.toString))
		}
		if (response.get(3).statusCode.good) {
			attributes.add(new Attribute(Attributes::ContainsNoLoops, response.get(3).value.toString))
		}
		if (response.get(4).statusCode.good) {
			attributes.add(new Attribute(Attributes::DataType, response.get(4).value.toString))
		}
		if (response.get(5).statusCode.good) {
			attributes.add(new Attribute(Attributes::Description, response.get(5).value.toString))
		}
		if (response.get(6).statusCode.good) {
			attributes.add(new Attribute(Attributes::DisplayName, response.get(6).value.toString))
		}
		if (response.get(7).statusCode.good) {
			attributes.add(new Attribute(Attributes::EventNotifier, response.get(7).value.toString))
		}
		if (response.get(8).statusCode.good) {
			attributes.add(new Attribute(Attributes::Executable, response.get(8).value.toString))
		}
		if (response.get(9).statusCode.good) {
			attributes.add(new Attribute(Attributes::Historizing, response.get(9).value.toString))
		}
		if (response.get(10).statusCode.good) {
			attributes.add(new Attribute(Attributes::InverseName, response.get(10).value.toString))
		}
		if (response.get(11).statusCode.good) {
			attributes.add(new Attribute(Attributes::IsAbstract, response.get(11).value.toString))
		}
		if (response.get(12).statusCode.good) {
			attributes.add(new Attribute(Attributes::MinimumSamplingInterval, response.get(12).value.toString))
		}
		if (response.get(13).statusCode.good) {
			attributes.add(new Attribute(Attributes::NodeClass, response.get(13).value.toString))
		}
		if (response.get(14).statusCode.good) {
			attributes.add(new Attribute(Attributes::NodeId, response.get(14).value.toString))
		}
		if (response.get(15).statusCode.good) {
			attributes.add(new Attribute(Attributes::Symmetric, response.get(15).value.toString))
		}
		if (response.get(16).statusCode.good) {
			attributes.add(new Attribute(Attributes::UserAccessLevel, response.get(16).value.toString))
		}
		if (response.get(17).statusCode.good) {
			attributes.add(new Attribute(Attributes::UserExecutable, response.get(17).value.toString))
		}
		if (response.get(18).statusCode.good) {
			attributes.add(new Attribute(Attributes::UserWriteMask, response.get(18).value.toString))
		}
		if (response.get(19).statusCode.good) {
			attributes.add(new Attribute(Attributes::Value, response.get(19).value.toString))
		}
		if (response.get(20).statusCode.good) {
			attributes.add(new Attribute(Attributes::ValueRank, response.get(20).value.toString))
		}
		if (response.get(21).statusCode.good) {
			attributes.add(new Attribute(Attributes::WriteMask, response.get(21).value.toString))
		}

	}
}