package com.server.dao.impl;

import com.server.dao.ECKeyPairDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
        this.getJdbcTemplate().update(sql, publicKeyByteData, privateKeyByteData);
        logger.info("Keypairs successfully registered. PublicKey:\n {}, PrivateKey:\n {}",
                Arrays.toString(publicKeyByteData), Arrays.toString(privateKeyByteData));
    }

    @Override
    public void deregisterKeyPair(byte[] targetPublicKeyByteData) {

    }

    @Override
    public byte[] getPrivateKeyByteData(byte[] targetPublicKeyByteData) {
        if (this.getJdbcTemplate() == null) throw new NullPointerException();
        String sql = "SELECT private_key FROM public.ec_keypair WHERE public_key = ?";
        byte[] privateKeyByteData = DataAccessUtils.singleResult(this.getJdbcTemplate().query(sql,
                ((resultSet, i) -> resultSet.getBytes(
                "private_key")), new Object[] {targetPublicKeyByteData}));
        logger.info("Retrieving Private Key from Public Key: \n {}", Arrays.toString(targetPublicKeyByteData));
        return privateKeyByteData;
    }
}
