/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.thrift;

import java.io.ByteArrayOutputStream;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TIOStreamTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * Generic utility for easily serializing objects into a byte array or Java
 * String.
 *
 */
public class TSerializer {

  /**
   * This is the byte array that data is actually serialized into
   */
  private final ByteArrayOutputStream baos_ = new ByteArrayOutputStream();

  /**
   * This transport wraps that byte array
   */
  private final TIOStreamTransport transport_;

  /**
   * Internal protocol used for serializing objects.
   */
  private TProtocol protocol_;

  /**
   * Create a new TSerializer that uses the TBinaryProtocol by default.
   *
   * @throws TTransportException
   */
  public TSerializer() throws TTransportException {
    this(new TBinaryProtocol.Factory());
  }

  /**
   * Create a new TSerializer. It will use the TProtocol specified by the
   * factory that is passed in.
   *
   * @param protocolFactory Factory to create a protocol
   * @throws TTransportException
   */
  public TSerializer(TProtocolFactory protocolFactory) throws TTransportException {
    transport_ = new TIOStreamTransport(new TConfiguration(), baos_);
    protocol_ = protocolFactory.getProtocol(transport_);
  }

  /**
   * Serialize the Thrift object into a byte array. The process is simple,
   * just clear the byte array output, write the object into it, and grab the
   * raw bytes.
   *
   * @param base The object to serialize
   * @return Serialized object in byte[] format
   * @throws TException if an error is encountered during serialization.
   */
  public byte[] serialize(TBase base) throws TException {
    baos_.reset();
    base.write(protocol_);
    return baos_.toByteArray();
  }

  /**
   * Serialize the Thrift object into a Java string, using the default JVM
   * charset encoding.
   *
   * @param base The object to serialize
   * @return Serialized object as a String
   * @throws TException if an error is encountered during serialization.
   */
  public String toString(TBase base) throws TException {
    return new String(serialize(base));
  }
}
