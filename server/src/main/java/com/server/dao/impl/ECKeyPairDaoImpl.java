package com.server.dao.impl;

import com.server.dao.ECKeyPairDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-07 4:41 p.m.
 */
@Component
public class ECKeyPairDaoImpl extends JdbcDaoSupport implements ECKeyPairDao {

    private final Logger logger = LoggerFactory.getLogger(ECKeyPairDaoImpl.class);

    public ECKeyPairDaoImpl(DataSource dataSource) {this.setDataSource(dataSource);}

    @Override
    public void registerKeyPair(byte[] publicKeyByteData, byte[] privateKeyByteData) {
        if (this.getJdbcTemplate() == null) throw new NullPointerException();
        String sql = "INSERT INTO public.ec_keypairs (public_key, private_key) VALUES (?, ?)";
        this.getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setBytes(1, publicKeyByteData);
            ps.setBytes(2, privateKeyByteData);
            return ps;
        });
        logger.debug("Keypairs successfully registered. PublicKey:\n {}, PrivateKey:\n {}",
                Arrays.toString(publicKeyByteData), Arrays.toString(privateKeyByteData));
    }

    @Override
    public void deregisterKeyPair(byte[] targetPublicKeyByteData) {
        if (this.getJdbcTemplate() == null) throw new NullPointerException();
        String sql = "DELETE FROM public.ec_keypairs WHERE public_key = ?";
        this.getJdbcTemplate().update(sql, new Object[] {targetPublicKeyByteData});
    }

    @Override
    public byte[] getPrivateKeyByteData(byte[] targetPublicKeyByteData) {
        if (this.getJdbcTemplate() == null) throw new NullPointerException();
        String sql = "SELECT * FROM public.ec_keypairs WHERE public_key = ?";
        byte[] privateKeyByteData = DataAccessUtils.singleResult(this.getJdbcTemplate().query(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setBytes(1, targetPublicKeyByteData);
                    return ps;
                },
                (resultSet, i) -> {

                    byte[] r = resultSet.getBytes("private_key");
                    logger.warn(Arrays.toString(r));
                    return r;
                }));
        logger.debug("Retrieving Private Key from Public Key: \n {}", Arrays.toString(targetPublicKeyByteData));
        return privateKeyByteData;

}
