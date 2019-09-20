package com.github.whitepin.sdk.contruct;

import java.util.Arrays;
import java.util.List;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import com.github.whitepin.sdk.client.FabricChannelClient;
import com.github.whitepin.sdk.client.FabricClientFactory;
import com.github.whitepin.sdk.context.FabricCAContext;
import com.github.whitepin.sdk.context.FabricOrdererContext;
import com.github.whitepin.sdk.context.FabricOrdererContext.FabricOrdererContextBuilder;
import com.github.whitepin.sdk.context.FabricPeerContext;
import com.github.whitepin.sdk.context.FabricPeerContext.FabricPeerContextBuilder;
import com.github.whitepin.sdk.context.FabricUserContext;

public class FabricContruct {

	// TODO :: properties로...
	// private final String caName = "ca0.caorg.test3";
	// private final String caLocation = "http://192.168.30.15:7054";
	// private final String userName = "caadmin@RootCA";
	// private final String userPassword = "1q2w3e4r5T";
	//
	// private final String orgMsp = "peerorg1";
	//
	// private final String ordererName = "orderer0.ordererorg.test3";
	// private final String ordererLocation = "grpc://192.168.30.16:7050";
	// private final String peerName1 = "peer0";
	// private final String peerLocation1 = "grpc://192.168.30.17:7051";
	// private final String peerName2 = "peer1";
	// private final String peerLocation2 = "grpc://192.168.30.18:7051";
	//
	// private final String channelName = "channel02";

	private String caName;
	private String caLocation;
	private String userName;
	private String userPassword;

	private String orgMsp;

	private String ordererName;
	private String ordererLocation;
	private String peerName1;
	private String peerLocation1;

	private String channelName;
	
	// TODO :: 임시...
	public Channel channel;
	public HFClient client;

	private List<FabricOrdererContext> fabricOrdererContexts;
	private List<FabricPeerContext> fabricPeerContexts;
	private FabricCAContext fabricCAContext;
	private FabricUserContext fabricUserContext;

	public FabricContruct(List<FabricOrdererContext> fabricOrdererContexts, List<FabricPeerContext> fabricPeerContexts,
			FabricCAContext fabricCAContext, FabricUserContext fabricUserContext, String channelName) {
		
		this.fabricOrdererContexts = fabricOrdererContexts;
		this.fabricPeerContexts = fabricPeerContexts;
		this.fabricCAContext = fabricCAContext;
		this.fabricUserContext = fabricUserContext;
		this.channelName = channelName;
	}

	public static FabricContructBuilder builder() {
		return new FabricContructBuilder();
	}
	
	public void initialize() throws Exception {
		FabricChannelClient FabricChannelClient = new FabricChannelClient();
		getHFClient();
		channel = FabricChannelClient.buildChannel(client, channelName, getFabricOrdererContexts(),
				getFabricPeerContexts());
		channel.initialize();
	}

	private void getHFClient() throws Exception {
		FabricClientFactory clientFactory = new FabricClientFactory();
		HFCAClient caClient = clientFactory.createCaClient(fabricCAContext.getName(), fabricCAContext.getLocation());
		Enrollment enroll = caClient.enroll(fabricCAContext.getUserName(), fabricCAContext.getUserPassword());

		FabricUserContext fabricUserContext = new FabricUserContext();
//		fabricUserContext.setAffiliation("peerorg1");
		fabricUserContext.setEnrollment(enroll);
		fabricUserContext.setAdmin(true);
		fabricUserContext.setMspId(fabricCAContext.getOrgMsp());
		fabricUserContext.setName(fabricCAContext.getUserName());

		client = clientFactory.createHFClient(fabricUserContext);
	}

	public static class FabricContructBuilder {
		private List<FabricOrdererContext> fabricOrdererContexts;
		private List<FabricPeerContext> fabricPeerContexts;
		private FabricCAContext fabricCAContext;
		private FabricUserContext fabricUserContext;
		private String channelName;
		
		public FabricContruct build() {
    		return new FabricContruct(fabricOrdererContexts, fabricPeerContexts, fabricCAContext, fabricUserContext, channelName);
    	}

		public FabricContruct.FabricContructBuilder fabricOrdererContexts(List<FabricOrdererContext> fabricOrdererContexts) {
			this.fabricOrdererContexts = fabricOrdererContexts;
			return this;
		}

		public FabricContruct.FabricContructBuilder fabricPeerContexts(List<FabricPeerContext> fabricPeerContexts) {
			this.fabricPeerContexts = fabricPeerContexts;
			return this;
		}

		public FabricContruct.FabricContructBuilder fabricUserContext(FabricUserContext fabricUserContext) {
			this.fabricUserContext = fabricUserContext;
			return this;
		}

		public FabricContruct.FabricContructBuilder fabricCAContext(FabricCAContext fabricCAContext) {
			this.fabricCAContext = fabricCAContext;
			return this;
		}

		public FabricContruct.FabricContructBuilder channelName(String channelName) {
			this.channelName = channelName;
			return this;
		}
		
	}

	
	/**
	 * TEMP.....
	 */
	public FabricContruct() {
		this.caName = "ca.whitepin.com";
		this.caLocation = "http://192.168.7.30:7054";
		this.userName = "Admin@Whitepin";
		this.userPassword = "WhitepinPWD";
		
		this.orgMsp = "WhitepinMSP";
		
		this.ordererName = "orderer0.whitepin.com";
		this.ordererLocation = "grpc://192.168.7.30:7050";
		this.peerName1 = "peer0.whitepin.com";
		this.peerLocation1 = "grpc://192.168.7.30:7051";
		
		this.channelName = "whitepin-main-channel";
	}

	public void setUp() throws Exception {
		FabricChannelClient FabricChannelClient = new FabricChannelClient();
		getHFClient1();
		channel = FabricChannelClient.buildChannel(client, channelName, getFabricOrdererContexts(),
				getFabricPeerContexts());
		channel.initialize();
	}

	private void getHFClient1() throws Exception {
		FabricClientFactory clientFactory = new FabricClientFactory();
		HFCAClient caClient = clientFactory.createCaClient(caName, caLocation);
		Enrollment enroll = caClient.enroll(userName, userPassword);

		FabricUserContext fabricUserContext = new FabricUserContext();
		fabricUserContext.setAffiliation(orgMsp);
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

//        FabricPeerContextBuilder fabricPeerContextBuilder2 = FabricPeerContext.builder();
//        fabricPeerContextBuilder2.name(peerName2);
//        fabricPeerContextBuilder2.location(peerLocation2);
//        fabricPeerContextBuilder2.properties(null);
//        FabricPeerContext peerContext2 = fabricPeerContextBuilder2.build();

//        return Arrays.asList(peerContext1, peerContext2);
		return Arrays.asList(peerContext1);
	}
	/////////////////////////////////

	public Channel getChannel() {
		return channel;
	}

	public HFClient getClient() {
		return client;
	}

}
