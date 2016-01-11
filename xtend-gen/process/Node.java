package process;

import com.prosysopc.ua.client.UaClient;
import java.util.ArrayList;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;
import process.Attribute;

/**
 * This class is the representation of a Process.
 * With the possibility to update it attributes
 * @autor Benoît Verhaeghe
 */
@SuppressWarnings("all")
public class Node {
  /**
   * The nodeId of the process to have a better access to it values
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private NodeId nodeId;
  
  /**
   * List of all process's attributes
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private ArrayList<Attribute> attributes;
  
  /**
   * @param the nodeId for this node
   */
  public Node(final NodeId nodeId) {
    this.nodeId = nodeId;
    ArrayList<Attribute> _arrayList = new ArrayList<Attribute>();
    this.attributes = _arrayList;
  }
  
  /**
   * Update all process's attributes's values and put new values to the list
   * @param client The client associate to the server for this process
   */
  public boolean updateAttributes(final UaClient client) {
    try {
      boolean _xblockexpression = false;
      {
        this.attributes.clear();
        ReadValueId[] nodesToRead = new ReadValueId[22];
        ReadValueId _readValueId = new ReadValueId(this.nodeId, Attributes.AccessLevel, null, null);
        nodesToRead[0] = _readValueId;
        ReadValueId _readValueId_1 = new ReadValueId(this.nodeId, Attributes.ArrayDimensions, null, null);
        nodesToRead[1] = _readValueId_1;
        ReadValueId _readValueId_2 = new ReadValueId(this.nodeId, Attributes.BrowseName, null, null);
        nodesToRead[2] = _readValueId_2;
        ReadValueId _readValueId_3 = new ReadValueId(this.nodeId, Attributes.ContainsNoLoops, null, null);
        nodesToRead[3] = _readValueId_3;
        ReadValueId _readValueId_4 = new ReadValueId(this.nodeId, Attributes.DataType, null, null);
        nodesToRead[4] = _readValueId_4;
        ReadValueId _readValueId_5 = new ReadValueId(this.nodeId, Attributes.Description, null, null);
        nodesToRead[5] = _readValueId_5;
        ReadValueId _readValueId_6 = new ReadValueId(this.nodeId, Attributes.DisplayName, null, null);
        nodesToRead[6] = _readValueId_6;
        ReadValueId _readValueId_7 = new ReadValueId(this.nodeId, Attributes.EventNotifier, null, null);
        nodesToRead[7] = _readValueId_7;
        ReadValueId _readValueId_8 = new ReadValueId(this.nodeId, Attributes.Executable, null, null);
        nodesToRead[8] = _readValueId_8;
        ReadValueId _readValueId_9 = new ReadValueId(this.nodeId, Attributes.Historizing, null, null);
        nodesToRead[9] = _readValueId_9;
        ReadValueId _readValueId_10 = new ReadValueId(this.nodeId, Attributes.InverseName, null, null);
        nodesToRead[10] = _readValueId_10;
        ReadValueId _readValueId_11 = new ReadValueId(this.nodeId, Attributes.IsAbstract, null, null);
        nodesToRead[11] = _readValueId_11;
        ReadValueId _readValueId_12 = new ReadValueId(this.nodeId, Attributes.MinimumSamplingInterval, null, null);
        nodesToRead[12] = _readValueId_12;
        ReadValueId _readValueId_13 = new ReadValueId(this.nodeId, Attributes.NodeClass, null, null);
        nodesToRead[13] = _readValueId_13;
        ReadValueId _readValueId_14 = new ReadValueId(this.nodeId, Attributes.NodeId, null, null);
        nodesToRead[14] = _readValueId_14;
        ReadValueId _readValueId_15 = new ReadValueId(this.nodeId, Attributes.Symmetric, null, null);
        nodesToRead[15] = _readValueId_15;
        ReadValueId _readValueId_16 = new ReadValueId(this.nodeId, Attributes.UserAccessLevel, null, null);
        nodesToRead[16] = _readValueId_16;
        ReadValueId _readValueId_17 = new ReadValueId(this.nodeId, Attributes.UserExecutable, null, null);
        nodesToRead[17] = _readValueId_17;
        ReadValueId _readValueId_18 = new ReadValueId(this.nodeId, Attributes.UserWriteMask, null, null);
        nodesToRead[18] = _readValueId_18;
        ReadValueId _readValueId_19 = new ReadValueId(this.nodeId, Attributes.Value, null, null);
        nodesToRead[19] = _readValueId_19;
        ReadValueId _readValueId_20 = new ReadValueId(this.nodeId, Attributes.ValueRank, null, null);
        nodesToRead[20] = _readValueId_20;
        ReadValueId _readValueId_21 = new ReadValueId(this.nodeId, Attributes.WriteMask, null, null);
        nodesToRead[21] = _readValueId_21;
        ReadResponse _read = client.read(UaClient.MAX_CACHE_AGE, TimestampsToReturn.Both, nodesToRead);
        DataValue[] response = _read.getResults();
        DataValue _get = response[0];
        StatusCode _statusCode = _get.getStatusCode();
        boolean _isGood = _statusCode.isGood();
        if (_isGood) {
          DataValue _get_1 = response[0];
          Variant _value = _get_1.getValue();
          String _string = _value.toString();
          Attribute _attribute = new Attribute(Attributes.AccessLevel, _string);
          this.attributes.add(_attribute);
        }
        DataValue _get_2 = response[1];
        StatusCode _statusCode_1 = _get_2.getStatusCode();
        boolean _isGood_1 = _statusCode_1.isGood();
        if (_isGood_1) {
          DataValue _get_3 = response[1];
          Variant _value_1 = _get_3.getValue();
          String _string_1 = _value_1.toString();
          Attribute _attribute_1 = new Attribute(Attributes.ArrayDimensions, _string_1);
          this.attributes.add(_attribute_1);
        }
        DataValue _get_4 = response[2];
        StatusCode _statusCode_2 = _get_4.getStatusCode();
        boolean _isGood_2 = _statusCode_2.isGood();
        if (_isGood_2) {
          DataValue _get_5 = response[2];
          Variant _value_2 = _get_5.getValue();
          String _string_2 = _value_2.toString();
          Attribute _attribute_2 = new Attribute(Attributes.BrowseName, _string_2);
          this.attributes.add(_attribute_2);
        }
        DataValue _get_6 = response[3];
        StatusCode _statusCode_3 = _get_6.getStatusCode();
        boolean _isGood_3 = _statusCode_3.isGood();
        if (_isGood_3) {
          DataValue _get_7 = response[3];
          Variant _value_3 = _get_7.getValue();
          String _string_3 = _value_3.toString();
          Attribute _attribute_3 = new Attribute(Attributes.ContainsNoLoops, _string_3);
          this.attributes.add(_attribute_3);
        }
        DataValue _get_8 = response[4];
        StatusCode _statusCode_4 = _get_8.getStatusCode();
        boolean _isGood_4 = _statusCode_4.isGood();
        if (_isGood_4) {
          DataValue _get_9 = response[4];
          Variant _value_4 = _get_9.getValue();
          String _string_4 = _value_4.toString();
          Attribute _attribute_4 = new Attribute(Attributes.DataType, _string_4);
          this.attributes.add(_attribute_4);
        }
        DataValue _get_10 = response[5];
        StatusCode _statusCode_5 = _get_10.getStatusCode();
        boolean _isGood_5 = _statusCode_5.isGood();
        if (_isGood_5) {
          DataValue _get_11 = response[5];
          Variant _value_5 = _get_11.getValue();
          String _string_5 = _value_5.toString();
          Attribute _attribute_5 = new Attribute(Attributes.Description, _string_5);
          this.attributes.add(_attribute_5);
        }
        DataValue _get_12 = response[6];
        StatusCode _statusCode_6 = _get_12.getStatusCode();
        boolean _isGood_6 = _statusCode_6.isGood();
        if (_isGood_6) {
          DataValue _get_13 = response[6];
          Variant _value_6 = _get_13.getValue();
          String _string_6 = _value_6.toString();
          Attribute _attribute_6 = new Attribute(Attributes.DisplayName, _string_6);
          this.attributes.add(_attribute_6);
        }
        DataValue _get_14 = response[7];
        StatusCode _statusCode_7 = _get_14.getStatusCode();
        boolean _isGood_7 = _statusCode_7.isGood();
        if (_isGood_7) {
          DataValue _get_15 = response[7];
          Variant _value_7 = _get_15.getValue();
          String _string_7 = _value_7.toString();
          Attribute _attribute_7 = new Attribute(Attributes.EventNotifier, _string_7);
          this.attributes.add(_attribute_7);
        }
        DataValue _get_16 = response[8];
        StatusCode _statusCode_8 = _get_16.getStatusCode();
        boolean _isGood_8 = _statusCode_8.isGood();
        if (_isGood_8) {
          DataValue _get_17 = response[8];
          Variant _value_8 = _get_17.getValue();
          String _string_8 = _value_8.toString();
          Attribute _attribute_8 = new Attribute(Attributes.Executable, _string_8);
          this.attributes.add(_attribute_8);
        }
        DataValue _get_18 = response[9];
        StatusCode _statusCode_9 = _get_18.getStatusCode();
        boolean _isGood_9 = _statusCode_9.isGood();
        if (_isGood_9) {
          DataValue _get_19 = response[9];
          Variant _value_9 = _get_19.getValue();
          String _string_9 = _value_9.toString();
          Attribute _attribute_9 = new Attribute(Attributes.Historizing, _string_9);
          this.attributes.add(_attribute_9);
        }
        DataValue _get_20 = response[10];
        StatusCode _statusCode_10 = _get_20.getStatusCode();
        boolean _isGood_10 = _statusCode_10.isGood();
        if (_isGood_10) {
          DataValue _get_21 = response[10];
          Variant _value_10 = _get_21.getValue();
          String _string_10 = _value_10.toString();
          Attribute _attribute_10 = new Attribute(Attributes.InverseName, _string_10);
          this.attributes.add(_attribute_10);
        }
        DataValue _get_22 = response[11];
        StatusCode _statusCode_11 = _get_22.getStatusCode();
        boolean _isGood_11 = _statusCode_11.isGood();
        if (_isGood_11) {
          DataValue _get_23 = response[11];
          Variant _value_11 = _get_23.getValue();
          String _string_11 = _value_11.toString();
          Attribute _attribute_11 = new Attribute(Attributes.IsAbstract, _string_11);
          this.attributes.add(_attribute_11);
        }
        DataValue _get_24 = response[12];
        StatusCode _statusCode_12 = _get_24.getStatusCode();
        boolean _isGood_12 = _statusCode_12.isGood();
        if (_isGood_12) {
          DataValue _get_25 = response[12];
          Variant _value_12 = _get_25.getValue();
          String _string_12 = _value_12.toString();
          Attribute _attribute_12 = new Attribute(Attributes.MinimumSamplingInterval, _string_12);
          this.attributes.add(_attribute_12);
        }
        DataValue _get_26 = response[13];
        StatusCode _statusCode_13 = _get_26.getStatusCode();
        boolean _isGood_13 = _statusCode_13.isGood();
        if (_isGood_13) {
          DataValue _get_27 = response[13];
          Variant _value_13 = _get_27.getValue();
          String _string_13 = _value_13.toString();
          Attribute _attribute_13 = new Attribute(Attributes.NodeClass, _string_13);
          this.attributes.add(_attribute_13);
        }
        DataValue _get_28 = response[14];
        StatusCode _statusCode_14 = _get_28.getStatusCode();
        boolean _isGood_14 = _statusCode_14.isGood();
        if (_isGood_14) {
          DataValue _get_29 = response[14];
          Variant _value_14 = _get_29.getValue();
          String _string_14 = _value_14.toString();
          Attribute _attribute_14 = new Attribute(Attributes.NodeId, _string_14);
          this.attributes.add(_attribute_14);
        }
        DataValue _get_30 = response[15];
        StatusCode _statusCode_15 = _get_30.getStatusCode();
        boolean _isGood_15 = _statusCode_15.isGood();
        if (_isGood_15) {
          DataValue _get_31 = response[15];
          Variant _value_15 = _get_31.getValue();
          String _string_15 = _value_15.toString();
          Attribute _attribute_15 = new Attribute(Attributes.Symmetric, _string_15);
          this.attributes.add(_attribute_15);
        }
        DataValue _get_32 = response[16];
        StatusCode _statusCode_16 = _get_32.getStatusCode();
        boolean _isGood_16 = _statusCode_16.isGood();
        if (_isGood_16) {
          DataValue _get_33 = response[16];
          Variant _value_16 = _get_33.getValue();
          String _string_16 = _value_16.toString();
          Attribute _attribute_16 = new Attribute(Attributes.UserAccessLevel, _string_16);
          this.attributes.add(_attribute_16);
        }
        DataValue _get_34 = response[17];
        StatusCode _statusCode_17 = _get_34.getStatusCode();
        boolean _isGood_17 = _statusCode_17.isGood();
        if (_isGood_17) {
          DataValue _get_35 = response[17];
          Variant _value_17 = _get_35.getValue();
          String _string_17 = _value_17.toString();
          Attribute _attribute_17 = new Attribute(Attributes.UserExecutable, _string_17);
          this.attributes.add(_attribute_17);
        }
        DataValue _get_36 = response[18];
        StatusCode _statusCode_18 = _get_36.getStatusCode();
        boolean _isGood_18 = _statusCode_18.isGood();
        if (_isGood_18) {
          DataValue _get_37 = response[18];
          Variant _value_18 = _get_37.getValue();
          String _string_18 = _value_18.toString();
          Attribute _attribute_18 = new Attribute(Attributes.UserWriteMask, _string_18);
          this.attributes.add(_attribute_18);
        }
        DataValue _get_38 = response[19];
        StatusCode _statusCode_19 = _get_38.getStatusCode();
        boolean _isGood_19 = _statusCode_19.isGood();
        if (_isGood_19) {
          DataValue _get_39 = response[19];
          Variant _value_19 = _get_39.getValue();
          String _string_19 = _value_19.toString();
          Attribute _attribute_19 = new Attribute(Attributes.Value, _string_19);
          this.attributes.add(_attribute_19);
        }
        DataValue _get_40 = response[20];
        StatusCode _statusCode_20 = _get_40.getStatusCode();
        boolean _isGood_20 = _statusCode_20.isGood();
        if (_isGood_20) {
          DataValue _get_41 = response[20];
          Variant _value_20 = _get_41.getValue();
          String _string_20 = _value_20.toString();
          Attribute _attribute_20 = new Attribute(Attributes.ValueRank, _string_20);
          this.attributes.add(_attribute_20);
        }
        boolean _xifexpression = false;
        DataValue _get_42 = response[21];
        StatusCode _statusCode_21 = _get_42.getStatusCode();
        boolean _isGood_21 = _statusCode_21.isGood();
        if (_isGood_21) {
          DataValue _get_43 = response[21];
          Variant _value_21 = _get_43.getValue();
          String _string_21 = _value_21.toString();
          Attribute _attribute_21 = new Attribute(Attributes.WriteMask, _string_21);
          _xifexpression = this.attributes.add(_attribute_21);
        }
        _xblockexpression = _xifexpression;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Pure
  public NodeId getNodeId() {
    return this.nodeId;
  }
  
  @Pure
  public ArrayList<Attribute> getAttributes() {
    return this.attributes;
  }
}
