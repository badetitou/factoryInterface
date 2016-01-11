package tools;

import com.google.common.base.Objects;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Attributes;

/**
 * This class is used for all translation between the number of an Attributes and his String representation
 * 
 * @author Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 */
@SuppressWarnings("all")
public class TranslateAttributeId {
  /**
   * @param typeOPCAttributes The number corresponding to the attributes following OPC UA Standard
   * @return A String user's readable
   */
  public static String getName(final UnsignedInteger typeOPCAttributes) {
    String _xifexpression = null;
    boolean _equals = Objects.equal(typeOPCAttributes, Attributes.AccessLevel);
    if (_equals) {
      _xifexpression = "AccessLevel";
    } else {
      String _xifexpression_1 = null;
      boolean _equals_1 = Objects.equal(typeOPCAttributes, Attributes.ArrayDimensions);
      if (_equals_1) {
        _xifexpression_1 = "ArrayDimensions";
      } else {
        String _xifexpression_2 = null;
        boolean _equals_2 = Objects.equal(typeOPCAttributes, Attributes.BrowseName);
        if (_equals_2) {
          _xifexpression_2 = "BrowseName";
        } else {
          String _xifexpression_3 = null;
          boolean _equals_3 = Objects.equal(typeOPCAttributes, Attributes.ContainsNoLoops);
          if (_equals_3) {
            _xifexpression_3 = "ContainsNoLoops";
          } else {
            String _xifexpression_4 = null;
            boolean _equals_4 = Objects.equal(typeOPCAttributes, Attributes.DataType);
            if (_equals_4) {
              _xifexpression_4 = "DataType";
            } else {
              String _xifexpression_5 = null;
              boolean _equals_5 = Objects.equal(typeOPCAttributes, Attributes.Description);
              if (_equals_5) {
                _xifexpression_5 = "Description";
              } else {
                String _xifexpression_6 = null;
                boolean _equals_6 = Objects.equal(typeOPCAttributes, Attributes.DisplayName);
                if (_equals_6) {
                  _xifexpression_6 = "DisplayName";
                } else {
                  String _xifexpression_7 = null;
                  boolean _equals_7 = Objects.equal(typeOPCAttributes, Attributes.EventNotifier);
                  if (_equals_7) {
                    _xifexpression_7 = "EventNotifier";
                  } else {
                    String _xifexpression_8 = null;
                    boolean _equals_8 = Objects.equal(typeOPCAttributes, Attributes.Executable);
                    if (_equals_8) {
                      _xifexpression_8 = "Executable";
                    } else {
                      String _xifexpression_9 = null;
                      boolean _equals_9 = Objects.equal(typeOPCAttributes, Attributes.Historizing);
                      if (_equals_9) {
                        _xifexpression_9 = "Historizing";
                      } else {
                        String _xifexpression_10 = null;
                        boolean _equals_10 = Objects.equal(typeOPCAttributes, Attributes.InverseName);
                        if (_equals_10) {
                          _xifexpression_10 = "InverseName";
                        } else {
                          String _xifexpression_11 = null;
                          boolean _equals_11 = Objects.equal(typeOPCAttributes, Attributes.IsAbstract);
                          if (_equals_11) {
                            _xifexpression_11 = "IsAbstract";
                          } else {
                            String _xifexpression_12 = null;
                            boolean _equals_12 = Objects.equal(typeOPCAttributes, Attributes.MinimumSamplingInterval);
                            if (_equals_12) {
                              _xifexpression_12 = "MinimumSamplingInterval";
                            } else {
                              String _xifexpression_13 = null;
                              boolean _equals_13 = Objects.equal(typeOPCAttributes, Attributes.NodeClass);
                              if (_equals_13) {
                                _xifexpression_13 = "NodeClass";
                              } else {
                                String _xifexpression_14 = null;
                                boolean _equals_14 = Objects.equal(typeOPCAttributes, Attributes.NodeId);
                                if (_equals_14) {
                                  _xifexpression_14 = "NodeId";
                                } else {
                                  String _xifexpression_15 = null;
                                  boolean _equals_15 = Objects.equal(typeOPCAttributes, Attributes.Symmetric);
                                  if (_equals_15) {
                                    _xifexpression_15 = "Symmetric";
                                  } else {
                                    String _xifexpression_16 = null;
                                    boolean _equals_16 = Objects.equal(typeOPCAttributes, Attributes.UserAccessLevel);
                                    if (_equals_16) {
                                      _xifexpression_16 = "UserAccessLevel";
                                    } else {
                                      String _xifexpression_17 = null;
                                      boolean _equals_17 = Objects.equal(typeOPCAttributes, Attributes.UserExecutable);
                                      if (_equals_17) {
                                        _xifexpression_17 = "UserExecutable";
                                      } else {
                                        String _xifexpression_18 = null;
                                        boolean _equals_18 = Objects.equal(typeOPCAttributes, Attributes.UserWriteMask);
                                        if (_equals_18) {
                                          _xifexpression_18 = "UserWriteMask";
                                        } else {
                                          String _xifexpression_19 = null;
                                          boolean _equals_19 = Objects.equal(typeOPCAttributes, Attributes.Value);
                                          if (_equals_19) {
                                            _xifexpression_19 = "Value";
                                          } else {
                                            String _xifexpression_20 = null;
                                            boolean _equals_20 = Objects.equal(typeOPCAttributes, Attributes.ValueRank);
                                            if (_equals_20) {
                                              _xifexpression_20 = "ValueRank";
                                            } else {
                                              String _xifexpression_21 = null;
                                              boolean _equals_21 = Objects.equal(typeOPCAttributes, Attributes.WriteMask);
                                              if (_equals_21) {
                                                _xifexpression_21 = "WriteMask";
                                              } else {
                                                _xifexpression_21 = "ERROR";
                                              }
                                              _xifexpression_20 = _xifexpression_21;
                                            }
                                            _xifexpression_19 = _xifexpression_20;
                                          }
                                          _xifexpression_18 = _xifexpression_19;
                                        }
                                        _xifexpression_17 = _xifexpression_18;
                                      }
                                      _xifexpression_16 = _xifexpression_17;
                                    }
                                    _xifexpression_15 = _xifexpression_16;
                                  }
                                  _xifexpression_14 = _xifexpression_15;
                                }
                                _xifexpression_13 = _xifexpression_14;
                              }
                              _xifexpression_12 = _xifexpression_13;
                            }
                            _xifexpression_11 = _xifexpression_12;
                          }
                          _xifexpression_10 = _xifexpression_11;
                        }
                        _xifexpression_9 = _xifexpression_10;
                      }
                      _xifexpression_8 = _xifexpression_9;
                    }
                    _xifexpression_7 = _xifexpression_8;
                  }
                  _xifexpression_6 = _xifexpression_7;
                }
                _xifexpression_5 = _xifexpression_6;
              }
              _xifexpression_4 = _xifexpression_5;
            }
            _xifexpression_3 = _xifexpression_4;
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  /**
   * @param string The string corresponding to Attribute
   * @see #getName(UnsignedInteger typeOPCAttributes)
   * @return The number corresponding to the string
   */
  public static UnsignedInteger getAttributeNumber(final String string) {
    UnsignedInteger _xifexpression = null;
    if ((string == "AccessLevel")) {
      _xifexpression = Attributes.AccessLevel;
    } else {
      UnsignedInteger _xifexpression_1 = null;
      if ((string == "ArrayDimensions")) {
        _xifexpression_1 = Attributes.ArrayDimensions;
      } else {
        UnsignedInteger _xifexpression_2 = null;
        if ((string == "BrowseName")) {
          _xifexpression_2 = Attributes.BrowseName;
        } else {
          UnsignedInteger _xifexpression_3 = null;
          if ((string == "ContainsNoLoops")) {
            _xifexpression_3 = Attributes.ContainsNoLoops;
          } else {
            UnsignedInteger _xifexpression_4 = null;
            if ((string == "DataType")) {
              _xifexpression_4 = Attributes.DataType;
            } else {
              UnsignedInteger _xifexpression_5 = null;
              if ((string == "Description")) {
                _xifexpression_5 = Attributes.Description;
              } else {
                UnsignedInteger _xifexpression_6 = null;
                if ((string == "DisplayName")) {
                  _xifexpression_6 = Attributes.DisplayName;
                } else {
                  UnsignedInteger _xifexpression_7 = null;
                  if ((string == "EventNotifier")) {
                    _xifexpression_7 = Attributes.EventNotifier;
                  } else {
                    UnsignedInteger _xifexpression_8 = null;
                    if ((string == "Executable")) {
                      _xifexpression_8 = Attributes.Executable;
                    } else {
                      UnsignedInteger _xifexpression_9 = null;
                      if ((string == "Historizing")) {
                        _xifexpression_9 = Attributes.Historizing;
                      } else {
                        UnsignedInteger _xifexpression_10 = null;
                        if ((string == "InverseName")) {
                          _xifexpression_10 = Attributes.InverseName;
                        } else {
                          UnsignedInteger _xifexpression_11 = null;
                          if ((string == "IsAbstract")) {
                            _xifexpression_11 = Attributes.IsAbstract;
                          } else {
                            UnsignedInteger _xifexpression_12 = null;
                            if ((string == "MinimumSamplingInterval")) {
                              _xifexpression_12 = Attributes.MinimumSamplingInterval;
                            } else {
                              UnsignedInteger _xifexpression_13 = null;
                              if ((string == "NodeClass")) {
                                _xifexpression_13 = Attributes.NodeClass;
                              } else {
                                UnsignedInteger _xifexpression_14 = null;
                                if ((string == "NodeId")) {
                                  _xifexpression_14 = Attributes.NodeId;
                                } else {
                                  UnsignedInteger _xifexpression_15 = null;
                                  if ((string == "Symmetric")) {
                                    _xifexpression_15 = Attributes.Symmetric;
                                  } else {
                                    UnsignedInteger _xifexpression_16 = null;
                                    if ((string == "UserAccessLevel")) {
                                      _xifexpression_16 = Attributes.UserAccessLevel;
                                    } else {
                                      UnsignedInteger _xifexpression_17 = null;
                                      if ((string == "UserExecutable")) {
                                        _xifexpression_17 = Attributes.UserExecutable;
                                      } else {
                                        UnsignedInteger _xifexpression_18 = null;
                                        if ((string == "UserWriteMask")) {
                                          _xifexpression_18 = Attributes.UserWriteMask;
                                        } else {
                                          UnsignedInteger _xifexpression_19 = null;
                                          if ((string == "Value")) {
                                            _xifexpression_19 = Attributes.Value;
                                          } else {
                                            UnsignedInteger _xifexpression_20 = null;
                                            if ((string == "ValueRank")) {
                                              _xifexpression_20 = Attributes.ValueRank;
                                            } else {
                                              _xifexpression_20 = Attributes.WriteMask;
                                            }
                                            _xifexpression_19 = _xifexpression_20;
                                          }
                                          _xifexpression_18 = _xifexpression_19;
                                        }
                                        _xifexpression_17 = _xifexpression_18;
                                      }
                                      _xifexpression_16 = _xifexpression_17;
                                    }
                                    _xifexpression_15 = _xifexpression_16;
                                  }
                                  _xifexpression_14 = _xifexpression_15;
                                }
                                _xifexpression_13 = _xifexpression_14;
                              }
                              _xifexpression_12 = _xifexpression_13;
                            }
                            _xifexpression_11 = _xifexpression_12;
                          }
                          _xifexpression_10 = _xifexpression_11;
                        }
                        _xifexpression_9 = _xifexpression_10;
                      }
                      _xifexpression_8 = _xifexpression_9;
                    }
                    _xifexpression_7 = _xifexpression_8;
                  }
                  _xifexpression_6 = _xifexpression_7;
                }
                _xifexpression_5 = _xifexpression_6;
              }
              _xifexpression_4 = _xifexpression_5;
            }
            _xifexpression_3 = _xifexpression_4;
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
}
