/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\android\\project\\learn\\WeiXinAddFriend\\src\\com\\xingqiba\\weixinaddfriend\\aidl\\ProcessConnection.aidl
 */
package com.xingqiba.weixinaddfriend.aidl;
public interface ProcessConnection extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.xingqiba.weixinaddfriend.aidl.ProcessConnection
{
private static final java.lang.String DESCRIPTOR = "com.xingqiba.weixinaddfriend.aidl.ProcessConnection";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.xingqiba.weixinaddfriend.aidl.ProcessConnection interface,
 * generating a proxy if needed.
 */
public static com.xingqiba.weixinaddfriend.aidl.ProcessConnection asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.xingqiba.weixinaddfriend.aidl.ProcessConnection))) {
return ((com.xingqiba.weixinaddfriend.aidl.ProcessConnection)iin);
}
return new com.xingqiba.weixinaddfriend.aidl.ProcessConnection.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getProcessName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getProcessName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.xingqiba.weixinaddfriend.aidl.ProcessConnection
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.lang.String getProcessName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getProcessName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getProcessName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public java.lang.String getProcessName() throws android.os.RemoteException;
}
