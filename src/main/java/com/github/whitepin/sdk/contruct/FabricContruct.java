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
import com.github.whitepin.sdk.context.FabricPeerContext;
import com.github.whitepin.sdk.context.FabricUserContext;

public class FabricContruct {

	//TODO :: properties로...
	private final String caName = "caName";
	private final String caLocation = "caLocation"; 
	private final String userName = "userName";
	private final String userPassword = "userPassword";
	
	private final String orgMsp = "orgMsp";
	
	private final String ordererName = "ordererName";
	private final String ordererLocation = "ordererLocation";
	private final String peerName1 = "peerName1";
	private final String peerLocation1 = "peerLocation1";
	private final String peerName2 = "peerName2";
	private final String peerLocation2 = "peerLocation2";
	
	private final String channelName = "channelName";
	
	//TODO :: 임시...
	public Channel channel;
	public HFClient client;
	
	public void setUp() throws Exception {
		FabricChannelClient FabricChannelClient = new FabricChannelClient();
		getHFClient();
		channel = FabricChannelClient.buildChannel(client, channelName, getFabricOrdererContexts(), getFabricPeerContexts());
		channel.initialize();
	}
	
	private void getHFClient() throws Exception {
		FabricClientFactory clientFactory = new FabricClientFactory();
		HFCAClient caClient = clientFactory.createCaClient(caName, caLocation);
		Enrollment enroll = caClient.enroll(userName, userPassword);
		
		FabricUserContext fabricUserContext = new FabricUserContext();
		fabricUserContext.setAffiliation(orgMsp);
		fabricUserContext.setEnrollment(enroll);
		fabricUserContext.setAdmin(true);
		fabricUserContext.setMspId(orgMsp);
		fabricUserContext.setName(userName);
		fabricUserContext.setPassword(userPassword);
		
		client = clientFactory.createHFClient(fabricUserContext);
	}
	
	private List<FabricOrdererContext> getFabricOrdererContexts() {
		FabricOrdererContext ordererContext = FabricOrdererContext.builder()
																	.name(ordererName)
																	.location(ordererLocation)
																	.properties(null)
																	.build();
		
		return Arrays.asList(ordererContext);
	}
	
	private List<FabricPeerContext> getFabricPeerContexts() {
		FabricPeerContext peerContext1 = FabricPeerContext.builder()
															.name(peerName1)
															.location(peerLocation1)
															.properties(null)
															.build();
		FabricPeerContext peerContext2 = FabricPeerContext.builder()
															.name(peerName2)
															.location(peerLocation2)
															.properties(null)
															.build();
		
		return Arrays.asList(peerContext1, peerContext2);
	}

	public Channel getChannel() {
		return channel;
	}

	public HFClient getClient() {
		return client;
	}
}