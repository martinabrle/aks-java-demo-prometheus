// https://github.com/Azure/prometheus-collector/blob/main/AddonBicepTemplate/README.md
param name string
param Location string
param azureMonitorWorkspaceResourceName string
param azureMonitorWorkspaceRG string
param azureMonitorWorkspaceSubscriptionId string
param tagsArray object

resource azureMonitorWorkspaceResource 'Microsoft.OperationalInsights/workspaces@2023-09-01' existing = {
  name: azureMonitorWorkspaceResourceName
  scope: resourceGroup( azureMonitorWorkspaceRG, azureMonitorWorkspaceSubscriptionId)
}

resource grafana 'Microsoft.Dashboard/grafana@2024-10-01' = {
  name: name
  tags: tagsArray
  sku: {
    name: 'Standard'
  }
  identity: {
    type: 'SystemAssigned'
  }
  location: Location
  properties: {
    grafanaIntegrations: {
      azureMonitorWorkspaceIntegrations: [
        {
          azureMonitorWorkspaceResourceId: azureMonitorWorkspaceResource.id
        }
      ]
    }
  }
}
