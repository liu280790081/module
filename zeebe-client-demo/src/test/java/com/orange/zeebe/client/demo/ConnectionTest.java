package com.orange.zeebe.client.demo;

import io.grpc.Grpc;
import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.DeploymentEvent;
import io.zeebe.client.api.response.WorkflowInstanceEvent;
import org.junit.Test;

public class ConnectionTest {

    private static final String zeebeAPI = "47.104.96.20:26500";
    private static final String clientId = "[Client ID]";
    private static final String clientSecret = "[Client Secret]";
    private static final String oAuthAPI = "[OAuth API] ";

    private ZeebeClient getZeebeClient(){
       return ZeebeClient.newClientBuilder().gatewayAddress(zeebeAPI).usePlaintext().build();
    }

    @Test
    public void test() {
//        OAuthCredentialsProvider credentialsProvider =
//                new OAuthCredentialsProviderBuilder()
//                        .authorizationServerUrl(oAuthAPI)
//                        .audience(zeebeAPI)
//                        .clientId(clientId)
//                        .clientSecret(clientSecret)
//                        .build();

        ZeebeClient client =
                ZeebeClient.newClientBuilder()
                        .gatewayAddress(zeebeAPI)
//                        .credentialsProvider(credentialsProvider)
                        .build();

        client.newTopologyRequest().send().join();

    }


    @Test
    public void deploymentSend() {
        ZeebeClient client = getZeebeClient();

        final DeploymentEvent deployment = client.newDeployCommand()
                .addResourceFromClasspath("diagram_1.bpmn")
                .send()
                .join();

        final int version = deployment.getWorkflows().get(0).getVersion();
        System.out.println("Workflow deployed. Version: " + version);
    }


    @Test
    public void startProcess() {
        ZeebeClient client = getZeebeClient();

        final WorkflowInstanceEvent wfInstance = client.newCreateInstanceCommand()
                .bpmnProcessId("Process_16qjavs")
                .latestVersion()
                .send()
                .join();

        final long workflowInstanceKey = wfInstance.getWorkflowInstanceKey();

        System.out.println("Workflow instance created. Key: " + workflowInstanceKey);

    }

}
