/**
 * Licensed to the Austrian Association for Software Tool Integration (AASTI)
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. The AASTI licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openengsb.ui.admin.testClient;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import junit.framework.Assert;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openengsb.core.api.AliveState;
import org.openengsb.core.api.Domain;
import org.openengsb.core.api.DomainProvider;
import org.openengsb.core.api.DomainService;
import org.openengsb.core.api.OsgiUtilsService;
import org.openengsb.core.api.ServiceManager;
import org.openengsb.core.api.WiringService;
import org.openengsb.core.api.context.ContextCurrentService;
import org.openengsb.core.api.context.ContextHolder;
import org.openengsb.core.api.descriptor.ServiceDescriptor;
import org.openengsb.core.api.l10n.LocalizableString;
import org.openengsb.core.api.l10n.PassThroughLocalizableString;
import org.openengsb.core.api.remote.ProxyFactory;
import org.openengsb.core.common.OpenEngSBCoreServices;
import org.openengsb.core.common.util.DefaultOsgiUtilsService;
import org.openengsb.core.services.internal.DefaultWiringService;
import org.openengsb.core.test.AbstractOsgiMockServiceTest;
import org.openengsb.ui.admin.connectorEditorPage.ConnectorEditorPage;
import org.openengsb.ui.admin.index.Index;
import org.openengsb.ui.admin.model.MethodCall;
import org.openengsb.ui.admin.model.MethodId;
import org.openengsb.ui.admin.model.OpenEngSBVersion;
import org.openengsb.ui.admin.model.ServiceId;
import org.openengsb.ui.common.OpenEngSBPage;
import org.openengsb.ui.common.editor.BeanEditorPanel;
import org.openengsb.ui.common.editor.fields.DropdownField;
import org.openengsb.ui.common.editor.fields.InputField;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class TestClientTest extends AbstractOsgiMockServiceTest {

    public interface AnotherTestInterface extends Domain {

    }

    public class TestService implements AnotherTestInterface {

        private String instanceId;

        public TestService(String id) {
            instanceId = id;
        }

        @Override
        public AliveState getAliveState() {
            return AliveState.OFFLINE;
        }

        @Override
        public String getInstanceId() {
            return instanceId;
        }
    }

    public enum UpdateEnum {
            ONE, TWO
    }

    private WicketTester tester;

    private ApplicationContextMock context;
    private TestInterface testService;
    private FormTester formTester;
    private boolean serviceListExpanded = true;
    private DomainService managedServicesMock;

    @Before
    public void setup() throws Exception {
        super.setUp();
        tester = new WicketTester();
        context = new ApplicationContextMock();
        context.putBean(mock(ContextCurrentService.class));
        context.putBean(bundleContext);
        context.putBean("openengsbVersion", new OpenEngSBVersion());
        context.putBean(mock(ProxyFactory.class));
    }

    @Test
    public void testLinkAppearsWithCaptionTestClient() throws Exception {
        setupIndexPage();

        tester.startPage(Index.class);

        tester.assertContains("Test Client");
    }

    @Test
    public void testParameterFormIsCreated() throws Exception {
        setupAndStartTestClientPage();

        tester.assertComponent("methodCallForm", Form.class);
    }

    @Test
    public void testServiceTreeIsCreated() throws Exception {
        setupAndStartTestClientPage();

        tester.assertComponent("methodCallForm:serviceList", LinkTree.class);
    }

    @Test
    public void testShowServiceInstancesInDropdown() throws Exception {
        List<ServiceReference> expected = setupAndStartTestClientPage();

        if (!serviceListExpanded) {
            expandServiceListTree();
        }
        for (int index = 2; index < expected.size() + 2; index++) {
            tester.assertComponent("methodCallForm:serviceList:i:" + index + ":nodeComponent:contentLink",
                    AjaxLink.class);
        }
    }

    @Test
    public void testCreateMethodListDropDown() throws Exception {
        setupAndStartTestClientPage();

        tester.assertComponent("methodCallForm:methodList", DropDownChoice.class);
    }

    @Test
    public void testServiceListSelect() throws Exception {
        setupAndStartTestClientPage();
        setServiceInDropDown(0);

        @SuppressWarnings("unchecked")
        Form<MethodCall> form = (Form<MethodCall>) tester.getComponentFromLastRenderedPage("methodCallForm");
        MethodCall modelObject = form.getModelObject();
        ServiceId reference = new ServiceId(TestInterface.class.getName(), "test");

        Assert.assertEquals(reference.toString(), modelObject.getService().toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testJumpToService() throws Exception {
        setupTestClientPage();
        ServiceId reference = new ServiceId(TestInterface.class.getName(), "test");
        tester.startPage(new TestClient(reference));
        tester.assertComponent("methodCallForm:serviceList:i:2:nodeComponent:contentLink:content", Label.class);
        Form<MethodCall> form = (Form<MethodCall>) tester.getComponentFromLastRenderedPage("methodCallForm");
        assertThat(form.getModelObject().getService(), is(reference));
        DropDownChoice<MethodId> ddc = (DropDownChoice<MethodId>) form.get("methodList");
        assertThat(ddc.getChoices().isEmpty(), is(false));
    }

    private void expandServiceListTree() {
        tester.clickLink("methodCallForm:serviceList:i:0:junctionLink", true);
        tester.clickLink("methodCallForm:serviceList:i:1:junctionLink", true);
        serviceListExpanded = true;
    }

    @Test
    public void testShowMethodListInDropDown() throws Exception {
        setupAndStartTestClientPage();
        @SuppressWarnings("unchecked")
        DropDownChoice<MethodId> methodList =
                (DropDownChoice<MethodId>) tester.getComponentFromLastRenderedPage("methodCallForm:methodList");

        setServiceInDropDown(0);

        List<? extends MethodId> choices = methodList.getChoices();
        List<Method> choiceMethods = new ArrayList<Method>();
        for (MethodId mid : choices) {
            choiceMethods.add(TestInterface.class.getMethod(mid.getName(), mid.getArgumentTypesAsClasses()));
        }
        Assert.assertEquals(Arrays.asList(TestInterface.class.getMethods()), choiceMethods);
    }

    @Test
    public void testCreateArgumentList() throws Exception {
        setupAndStartTestClientPage();

        Component argList =
                tester.getComponentFromLastRenderedPage("methodCallForm:argumentListContainer:argumentList");

        Assert.assertNotNull(argList);
    }

    @Test
    public void testCreateTextFieldsFor2StringArguments() throws Exception {
        setupAndStartTestClientPage();
        RepeatingView argList =
                (RepeatingView) tester
                        .getComponentFromLastRenderedPage("methodCallForm:argumentListContainer:argumentList");

        setServiceInDropDown(0);
        setMethodInDropDown(0);
        Assert.assertEquals(2, argList.size());
        Iterator<? extends Component> iterator = argList.iterator();
        while (iterator.hasNext()) {
            Component next = iterator.next();
            tester.assertComponent(next.getPageRelativePath() + ":valueEditor", InputField.class);
        }
    }

    @Test
    public void testCreateDropdownForOptionArguments() throws Exception {
        setupAndStartTestClientPage();
        RepeatingView argList =
                (RepeatingView) tester
                        .getComponentFromLastRenderedPage("methodCallForm:argumentListContainer:argumentList");

        setServiceInDropDown(0);
        setMethodInDropDown(2);

        Assert.assertEquals(1, argList.size());
        tester.assertComponent("methodCallForm:argumentListContainer:argumentList:arg0panel:valueEditor",
                DropdownField.class);
    }

    private void setMethodInDropDown(int index) {
        formTester.select("methodList", index);
        tester.executeAjaxEvent("methodCallForm:methodList", "onchange");
    }

    @Test
    public void testCreateTextFieldsForBean() throws Exception {
        setupAndStartTestClientPage();
        RepeatingView argList =
                (RepeatingView) tester
                        .getComponentFromLastRenderedPage("methodCallForm:argumentListContainer:argumentList");

        setServiceInDropDown(0);
        setMethodInDropDown(1);

        Assert.assertEquals(1, argList.size());
        Assert.assertEquals(BeanEditorPanel.class, argList.get("arg0panel:valueEditor").getClass());
        tester.debugComponentTrees();
        RepeatingView panel = (RepeatingView) argList.get("arg0panel:valueEditor:fields");
        Assert.assertEquals(2, panel.size());
    }

    @Test
    public void testPerformMethodCall() throws Exception {
        setupAndStartTestClientPage();
        RepeatingView argList =
                (RepeatingView) tester
                        .getComponentFromLastRenderedPage("methodCallForm:argumentListContainer:argumentList");

        setServiceInDropDown(0);
        setMethodInDropDown(0);
        tester.debugComponentTrees();
        for (int i = 0; i < argList.size(); i++) {
            formTester.setValue("argumentListContainer:argumentList:arg" + i + "panel:valueEditor:field", "test");
        }

        tester.executeAjaxEvent("methodCallForm:submitButton", "onclick");
        verify(testService).update("test", "test");
    }

    @Test
    public void testPerformMethodCallWithBeanArgument() throws Exception {
        setupAndStartTestClientPage();

        setServiceInDropDown(0);
        setMethodInDropDown(1);

        String beanPanelPath = "argumentListContainer:argumentList:arg0panel:valueEditor";
        BeanEditorPanel beanPanel =
                (BeanEditorPanel) tester.getComponentFromLastRenderedPage("methodCallForm:" + beanPanelPath);
        String idFieldId = beanPanel.getFieldViewId("id");
        String nameFieldId = beanPanel.getFieldViewId("name");
        formTester.setValue(beanPanelPath + ":fields:" + idFieldId + ":row:field", "42");
        formTester.setValue(beanPanelPath + ":fields:" + nameFieldId + ":row:field", "test");

        tester.executeAjaxEvent("methodCallForm:submitButton", "onclick");
        verify(testService).update(new TestBean("42", "test"));
    }

    @Test
    public void testPerformMethodCallWithIntegerObjectArgument() throws Exception {
        setupAndStartTestClientPage();

        setServiceInDropDown(0);
        setMethodInDropDown(3);

        String beanPanelPath = "argumentListContainer:argumentList:arg0panel:valueEditor";
        tester.debugComponentTrees();
        formTester.setValue(beanPanelPath + ":field", "42");

        tester.executeAjaxEvent("methodCallForm:submitButton", "onclick");
        verify(testService).update(new Integer(42));
    }

    private void setServiceInDropDown(int index) {
        if (!serviceListExpanded) {
            expandServiceListTree();
        }
        tester.clickLink("methodCallForm:serviceList:i:" + (index + 3) + ":nodeComponent:contentLink", true);
        tester.executeAjaxEvent("methodCallForm:serviceList:i:" + (index + 3) + ":nodeComponent:contentLink",
                "onclick");
    }

    @Test
    public void testSelectMethodTwice() throws Exception {
        setupAndStartTestClientPage();
        RepeatingView argList =
                (RepeatingView) tester
                        .getComponentFromLastRenderedPage("methodCallForm:argumentListContainer:argumentList");

        setServiceInDropDown(0);
        setMethodInDropDown(0);
        tester.executeAjaxEvent("methodCallForm:methodList", "onchange");

        Assert.assertEquals(2, argList.size());
    }

    @Test
    public void testFormResetAfterCall() throws Exception {
        setupAndStartTestClientPage();

        setServiceInDropDown(0);
        setMethodInDropDown(0);

        formTester.setValue("argumentListContainer:argumentList:arg0panel:valueEditor:field", "test");
        formTester.setValue("argumentListContainer:argumentList:arg1panel:valueEditor:field", "test");
        tester.executeAjaxEvent("methodCallForm:submitButton", "onclick");

        RepeatingView argList =
                (RepeatingView) tester
                        .getComponentFromLastRenderedPage("methodCallForm:argumentListContainer:argumentList");
        Assert.assertEquals(0, argList.size());
    }

    @Test
    public void testFeedbackPanelIsPresent() throws Exception {
        setupAndStartTestClientPage();
        tester.assertComponent("feedback", FeedbackPanel.class);
    }

    @Test
    public void testFeedbackPanelContainsText() throws Exception {
        setupAndStartTestClientPage();

        setServiceInDropDown(0);
        setMethodInDropDown(0);
        formTester.setValue("argumentListContainer:argumentList:arg0panel:valueEditor:field", "test");
        formTester.setValue("argumentListContainer:argumentList:arg1panel:valueEditor:field", "test");
        tester.executeAjaxEvent("methodCallForm:submitButton", "onclick");

        FeedbackPanel feedbackPanel = (FeedbackPanel) tester.getComponentFromLastRenderedPage("feedback");
        tester.assertInfoMessages(new String[]{ "Methodcall called successfully" });
        Label message = (Label) feedbackPanel.get("feedbackul:messages:0:message");
        Assert.assertEquals("Methodcall called successfully", message.getDefaultModelObjectAsString());
    }

    @Test
    public void testExceptionInFeedback() throws Exception {
        setupAndStartTestClientPage();

        setServiceInDropDown(0);
        setMethodInDropDown(0);
        formTester.setValue("argumentListContainer:argumentList:arg0panel:valueEditor:field", "fail");
        formTester.setValue("argumentListContainer:argumentList:arg1panel:valueEditor:field", "test");
        tester.executeAjaxEvent("methodCallForm:submitButton", "onclick");
        String resultException = (String) tester.getMessages(FeedbackMessage.ERROR).get(0);
        assertThat(resultException, containsString(IllegalArgumentException.class.getName()));
    }

    @Test
    public void testSubmitButtonIslocalized() throws Exception {
        setupAndStartTestClientPage();

        Button button = (Button) tester.getComponentFromLastRenderedPage("methodCallForm:submitButton");
        String buttonValue =
                tester.getApplication().getResourceSettings().getLocalizer().getString("form.call", button);
        Assert.assertEquals(buttonValue, button.getValue());
    }

    @Test
    public void testSelectOtherService_shouldClearArgumentList() throws Exception {
        setupAndStartTestClientPage();
        setServiceInDropDown(0);
        setMethodInDropDown(0);
        setServiceInDropDown(0);
        RepeatingView argList =
                (RepeatingView) tester
                        .getComponentFromLastRenderedPage("methodCallForm:argumentListContainer:argumentList");
        assertThat(argList.size(), is(0));
    }

    private void setupTesterWithSpringMockContext() {
        tester.getApplication().addComponentInstantiationListener(
            new SpringComponentInjector(tester.getApplication(), context, true));
    }

    @Test
    public void testListToCreateNewServices() throws Exception {
        setupAndStartTestClientPage();
        tester.debugComponentTrees();
        tester.assertRenderedPage(TestClient.class);
        Label domainName =
                (Label) tester.getComponentFromLastRenderedPage("serviceManagementContainer:domains:0:domain.name");
        Label domainDescription =
                (Label) tester
                        .getComponentFromLastRenderedPage("serviceManagementContainer:domains:0:domain.description");
        Label domainClass =
                (Label) tester.getComponentFromLastRenderedPage("serviceManagementContainer:domains:0:domain.class");
        Label name =
                (Label) tester
                        .getComponentFromLastRenderedPage(
                                "serviceManagementContainer:domains:0:services:0:service.name");
        Label description =
                (Label) tester.getComponentFromLastRenderedPage("serviceManagementContainer:domains:"
                        + "0:services:0:service.description");
        assertThat(domainName.getDefaultModel().getObject().toString(), equalTo("testDomain"));
        assertThat(domainDescription.getDefaultModel().getObject().toString(), equalTo("testDomain"));
        assertThat(domainClass.getDefaultModel().getObject().toString(), equalTo(TestInterface.class.getName()));
        Assert.assertEquals("service.name", name.getDefaultModel().getObject());
        Assert.assertEquals("service.description", description.getDefaultModel().getObject());
    }

    @Test
    public void showEditLink() throws Exception {
        List<ServiceReference> expected = setupAndStartTestClientPage();
        if (!serviceListExpanded) {
            expandServiceListTree();
        }
        for (int index = 2; index < expected.size() + 2; index++) {
            tester.assertComponent("methodCallForm:serviceList:i:" + index + ":nodeComponent:contentLink",
                    AjaxLink.class);
        }
        tester.assertComponent("methodCallForm:editButton", AjaxButton.class);
        AjaxButton editButton = (AjaxButton) tester.getComponentFromLastRenderedPage("methodCallForm:editButton");
        // should be disabled when nothing is selected
        Assert.assertEquals(false, editButton.isEnabled());
    }

    @Test
    public void testTargetLocationOfEditButton() throws Exception {
        setupAndStartTestClientPage();
        ServiceReference ref = Mockito.mock(ServiceReference.class);
        Mockito.when(ref.getProperty("managerId")).thenReturn("ManagerId");
        Mockito.when(ref.getProperty("domain")).thenReturn(TestInterface.class.getName());
        when(bundleContext.getServiceReferences(Domain.class.getName(), String.format("(id=%s)", "test"))).thenReturn(
            new ServiceReference[]{ ref });

        List<ServiceManager> managerList = new ArrayList<ServiceManager>();
        ServiceManager serviceManagerMock = Mockito.mock(ServiceManager.class);
        ServiceDescriptor serviceDescriptor = Mockito.mock(ServiceDescriptor.class);
        Mockito.when(serviceDescriptor.getId()).thenReturn("ManagerId");
        Mockito.when(serviceDescriptor.getName()).thenReturn(new PassThroughLocalizableString("ServiceName"));
        Mockito.when(serviceDescriptor.getDescription()).thenReturn(
                new PassThroughLocalizableString("ServiceDescription"));

        Mockito.when(serviceManagerMock.getDescriptor()).thenReturn(serviceDescriptor);
        Mockito.when(serviceManagerMock.getDescriptor()).thenReturn(serviceDescriptor);

        managerList.add(serviceManagerMock);
        Mockito.when(managedServicesMock.serviceManagersForDomain(TestInterface.class)).thenReturn(managerList);

        if (!serviceListExpanded) {
            expandServiceListTree();
        }
        tester.debugComponentTrees();
        tester.clickLink("methodCallForm:serviceList:i:3:nodeComponent:contentLink", true);
        AjaxButton editButton = (AjaxButton) tester.getComponentFromLastRenderedPage("methodCallForm:editButton");
        Assert.assertEquals(true, editButton.isEnabled());
        tester.executeAjaxEvent(editButton, "onclick");

        ConnectorEditorPage editorPage = Mockito.mock(ConnectorEditorPage.class);
        tester.assertRenderedPage(editorPage.getPageClass());
    }

    @Test
    public void testStartWithContextAsParam() throws Exception {
        setupTestClientPage();
        ContextHolder.get().setCurrentContextId("foo2");
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put(OpenEngSBPage.CONTEXT_PARAM, new String[]{ "foo" });
        tester.startPage(TestClient.class, new PageParameters(parameterMap));
        assertThat(ContextHolder.get().getCurrentContextId(), is("foo"));
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testForEachDomainVisibleInCreatePartIsAnEntryInTree() throws Exception {
        setupAndStartTestClientPage();
        tester.assertRenderedPage(TestClient.class);
        List<String> domains = new ArrayList<String>();
        List<String> availableInTree = new ArrayList<String>();
        List<DefaultMutableTreeNode> availableInTreeAsTreeNode = new ArrayList<DefaultMutableTreeNode>();

        int count = ((ArrayList) tester.getComponentFromLastRenderedPage("serviceManagementContainer:domains")
                .getDefaultModelObject()).size();
        // get all domains
        tester.debugComponentTrees();
        for (int i = 0; i < count; i++) {
            Component label = tester
                    .getComponentFromLastRenderedPage("serviceManagementContainer:domains:" + i + ":domain.name");
            domains.add(label.getDefaultModelObjectAsString());
        }

        // get all services from the tree
        DefaultTreeModel serviceListTree = (DefaultTreeModel) tester
                .getComponentFromLastRenderedPage("methodCallForm:serviceList").getDefaultModelObject();
        count = serviceListTree.getChildCount(serviceListTree.getRoot());
        for (int i = 0; i < count; i++) {
            DefaultMutableTreeNode child =
                    (DefaultMutableTreeNode) serviceListTree.getChild(serviceListTree.getRoot(), i);
            String userObject = (String) child.getUserObject();
            availableInTreeAsTreeNode.add(child);
            availableInTree.add(userObject);
        }

        for (int i = 0; i < domains.size(); i++) {
            String domain = domains.get(i);
            assertThat(availableInTree.contains(domain), is(true));
            assertThat(serviceListTree.getChildCount(availableInTreeAsTreeNode.get(i)), greaterThan(0));
        }
    }

    @Test
    public void testToSelectDefaultEndPoint_ShouldDisplayDomainMethodWithArguments() throws Exception {
        setupAndStartTestClientPage();
        tester.assertRenderedPage(TestClient.class);

        setServiceInDropDown(-1);
        setMethodInDropDown(0);
        RepeatingView argList =
                (RepeatingView) tester
                        .getComponentFromLastRenderedPage("methodCallForm:argumentListContainer:argumentList");
        Assert.assertEquals(2, argList.size());

    }

    @Test
    public void testErrorMessageAppearIfServiceDoesNotExists() throws Exception {
        setupAndStartTestClientPage();
        tester.assertRenderedPage(TestClient.class);

        setServiceInDropDown(2);
        tester.debugComponentTrees();
        AjaxButton submitButton = (AjaxButton) tester.getComponentFromLastRenderedPage("methodCallForm:submitButton");
        assertFalse(submitButton.isEnabled());
        String resultException = (String) tester.getMessages(FeedbackMessage.ERROR).get(0);
        assertThat(resultException, containsString("No service found for domain"));
        assertThat(resultException, containsString(AnotherTestInterface.class.getName()));

    }

    private List<ServiceReference> setupAndStartTestClientPage() throws Exception {
        final List<ServiceReference> expected = setupTestClientPage();
        tester.startPage(TestClient.class);
        formTester = tester.newFormTester("methodCallForm");
        return expected;
    }

    private List<ServiceReference> setupTestClientPage() throws Exception {
        final List<ServiceReference> expected = new ArrayList<ServiceReference>();
        ServiceReference serviceReferenceMock = mock(ServiceReference.class);
        when(serviceReferenceMock.getProperty("id")).thenReturn("test");
        expected.add(serviceReferenceMock);
        managedServicesMock = mock(DomainService.class);
        when(managedServicesMock.getAllServiceInstances()).thenAnswer(new Answer<List<ServiceReference>>() {
            @Override
            public List<ServiceReference> answer(InvocationOnMock invocation) {
                return expected;
            }
        });
        final List<DomainProvider> expectedProviders = new ArrayList<DomainProvider>();
        List<DomainProvider> domainProviderMocks = createDomainProviderMocks();
        expectedProviders.addAll(domainProviderMocks);
        when(managedServicesMock.domains()).thenAnswer(new Answer<List<DomainProvider>>() {
            @Override
            public List<DomainProvider> answer(InvocationOnMock invocation) {
                return expectedProviders;
            }
        });

        Mockito.when(managedServicesMock.serviceReferencesForDomain(TestInterface.class)).thenReturn(expected);

        ServiceManager serviceManagerMock = Mockito.mock(ServiceManager.class);
        List<ServiceManager> serviceManagerList = new ArrayList<ServiceManager>();
        serviceManagerList.add(serviceManagerMock);
        Mockito.when(managedServicesMock.serviceManagersForDomain(TestInterface.class)).thenReturn(serviceManagerList);

        ServiceDescriptor serviceDescriptorMock = Mockito.mock(ServiceDescriptor.class);
        Mockito.when(serviceDescriptorMock.getName()).thenReturn(new PassThroughLocalizableString("service.name"));
        Mockito.when(serviceDescriptorMock.getDescription()).thenReturn(
                new PassThroughLocalizableString("service.description"));
        Mockito.when(serviceManagerMock.getDescriptor()).thenReturn(serviceDescriptorMock);

        testService = mock(TestInterface.class);
        registerServiceViaId(testService, "test", TestInterface.class);

        doThrow(new IllegalArgumentException()).when(testService).update(eq("fail"), anyString());
        when(managedServicesMock.getService(any(ServiceReference.class))).thenReturn(testService);
        when(managedServicesMock.getService(anyString(), anyString())).thenReturn(testService);
        context.putBean(managedServicesMock);
        setupTesterWithSpringMockContext();
        return expected;
    }

    private List<DomainProvider> createDomainProviderMocks() {
        List<DomainProvider> expectedProviders = new ArrayList<DomainProvider>();
        DomainProvider domainProviderMock = mock(DomainProvider.class);
        LocalizableString testDomainLocalziedStringMock = mock(LocalizableString.class);
        when(testDomainLocalziedStringMock.getString(Mockito.<Locale> any())).thenReturn("testDomain");
        when(domainProviderMock.getName()).thenReturn(testDomainLocalziedStringMock);
        when(domainProviderMock.getDescription()).thenReturn(testDomainLocalziedStringMock);
        when(domainProviderMock.getDomainInterface()).thenAnswer(new Answer<Class<? extends Domain>>() {
            @Override
            public Class<? extends Domain> answer(InvocationOnMock invocation) {
                return TestInterface.class;
            }
        });
        expectedProviders.add(domainProviderMock);
        DomainProvider anotherDomainProviderMock = mock(DomainProvider.class);
        LocalizableString anotherTestDomainLocalziedStringMock = mock(LocalizableString.class);
        when(anotherTestDomainLocalziedStringMock.getString(Mockito.<Locale> any())).thenReturn("anotherTestDomain");
        when(anotherDomainProviderMock.getName()).thenReturn(anotherTestDomainLocalziedStringMock);
        when(anotherDomainProviderMock.getDescription()).thenReturn(anotherTestDomainLocalziedStringMock);
        when(anotherDomainProviderMock.getDomainInterface()).thenAnswer(new Answer<Class<? extends Domain>>() {
            @Override
            public Class<? extends Domain> answer(InvocationOnMock invocation) {
                return AnotherTestInterface.class;
            }
        });
        expectedProviders.add(anotherDomainProviderMock);

        return expectedProviders;
    }

    private void setupIndexPage() {
        DomainService domainServiceMock = mock(DomainService.class);
        context.putBean(domainServiceMock);
        setupTesterWithSpringMockContext();
    }

    @Override
    protected void setBundleContext(BundleContext bundleContext) {
        DefaultOsgiUtilsService osgiServiceUtils = new DefaultOsgiUtilsService();
        osgiServiceUtils.setBundleContext(bundleContext);
        registerService(osgiServiceUtils, new Hashtable<String, Object>(), OsgiUtilsService.class);
        DefaultWiringService defaultWiringService = new DefaultWiringService();
        defaultWiringService.setBundleContext(bundleContext);
        registerService(defaultWiringService, new Hashtable<String, Object>(), WiringService.class);
        OpenEngSBCoreServices.setOsgiServiceUtils(osgiServiceUtils);
    }
}