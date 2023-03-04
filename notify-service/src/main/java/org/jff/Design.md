1. 统一所有接口
2. 假设有多种通知服务，每个通知服务自行组织具体的通知内容

SendNotification

NotificationEvent
+ recipient
+ activity
  + type - comment
  + category
  + publisher
  + content
