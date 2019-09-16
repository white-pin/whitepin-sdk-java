package com.github.whitepin.sdk.contruct;

import java.util.Arrays;
import java.util.List;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import com.github.whitepin.sdk.client.FabricChannelClient;
import com.github.whitepin.sdk.client.FabricClientFactory;
import com.github.whitepin.sdk.context.FabricOrdererContext;
import com.github.whitepin.sdk.context.FabricOrdererContext.FabricOrdererContextBuilder;
import com.github.whitepin.sdk.context.FabricPeerContext;
import com.github.whitepin.sdk.context.FabricPeerContext.FabricPeerContextBuilder;
import com.github.whitepin.sdk.context.FabricUserContext;

public class FabricContruct {

    //TODO :: properties로...
    //	private final String caName = "ca0.caorg.test3";
    //	private final String caLocation = "http://192.168.30.15:7054"; 
    //	private final String userName = "caadmin@RootCA"; 
    //	private final String userPassword = "1q2w3e4r5T";
    //	
    //	private final String orgMsp = "peerorg1";
    //	
    //	private final String ordererName = "orderer0.ordererorg.test3"; 
    //	private final String ordererLocation = "grpc://192.168.30.16:7050";
    //	private final String peerName1 = "peer0";
    //	private final String peerLocation1 = "grpc://192.168.30.17:7051";
    //	private final String peerName2 = "peer1";
    //	private final String peerLocation2 = "grpc://192.168.30.18:7051";
    //	
    //	private final String channelName = "channel02";

    private final String caName = "ca.testnet.com";
    private final String caLocation = "http://192.168.7.30:7054";
    private final String userName = "Admin@PeerOrg1";
    private final String userPassword = "PeerOrg1PWD";

    private final String orgMsp = "PeerOrg1MSP";

    private final String ordererName = "orderer0.ordererorg.testnet.com";
    private final String ordererLocation = "grpc://192.168.7.30:7050";
    private final String peerName1 = "peer0.peerorg1.testnet.com";
    private final String peerLocation1 = "grpc://192.168.7.30:7051";
    private final String peerName2 = "peer1.peerorg1.testnet.com";
    private final String peerLocation2 = "grpc://192.168.7.31:7051";

    private final String channelName = "ch1";

    //TODO :: 임시...
    public Channel channel;
    public HFClient client;

    public void setUp() throws Exception {
        FabricChannelClient FabricChannelClient = new FabricChannelClient();
        getHFClient();
        channel = FabricChannelClient.buildChannel(client, channelName, getFabricOrdererContexts(),
                getFabricPeerContexts());
        channel.initialize();
    }

    private void getHFClient() throws Exception {
        FabricClientFactory clientFactory = new FabricClientFactory();
        HFCAClient caClient = clientFactory.createCaClient(caName, caLocation);
        Enrollment enroll = caClient.enroll(userName, userPassword);

        FabricUserContext fabricUserContext = new FabricUserContext();
        fabricUserContext.setAffiliation("peerorg1");
        fabricUserContext.setEnrollment(enroll);
        fabricUserContext.setAdmin(true);
        fabricUserContext.setMspId(orgMsp);
        fabricUserContext.setName(userName);

        client = clientFactory.createHFClient(fabricUserContext);
    }

    private List<FabricOrdererContext> getFabricOrdererContexts() {
        FabricOrdererContextBuilder fabricOrdererContextBuilder = FabricOrdererContext.builder();
        fabricOrdererContextBuilder.name(ordererName);
        fabricOrdererContextBuilder.location(ordererLocation);
        fabricOrdererContextBuilder.properties(null);
        FabricOrdererContext ordererContext = fabricOrdererContextBuilder.build();

        return Arrays.asList(ordererContext);
    }

    private List<FabricPeerContext> getFabricPeerContexts() {
        FabricPeerContextBuilder fabricPeerContextBuilder1 = FabricPeerContext.builder();
        fabricPeerContextBuilder1.name(peerName1);
        fabricPeerContextBuilder1.location(peerLocation1);
        fabricPeerContextBuilder1.properties(null);
        FabricPeerContext peerContext1 = fabricPeerContextBuilder1.build();

        FabricPeerContextBuilder fabricPeerContextBuilder2 = FabricPeerContext.builder();
        fabricPeerContextBuilder2.name(peerName2);
        fabricPeerContextBuilder2.location(peerLocation2);
        fabricPeerContextBuilder2.properties(null);
        FabricPeerContext peerContext2 = fabricPeerContextBuilder2.build();

        return Arrays.asList(peerContext1, peerContext2);
    }

    public Channel getChannel() {
        return channel;
    }

    public HFClient getClient() {
        return client;
    }
}
