package com.server.dao;

import org.springframework.stereotype.Repository;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-07 3:33 p.m.
 */
@Repository
public interface ECKeyPairDao {
    public void registerKeyPair(byte[] publicKeyByteData, byte[] privateKeyByteData);
    public void deregisterKeyPair(byte[] targetPublicKeyByteData);
    public byte[] getPrivateKeyByteData(byte[] targetPublicKeyByteData);
}
