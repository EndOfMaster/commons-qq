# qq login java sdk

## 步骤
1. 构建跳转地址然后跳转，这里跳转到qq的页面了
2. 用户如果点了授权，qq就会跳转到之前构建是传的回调地址
3. 然后用给的code获取accessToken
4. 用accessToken获取openId
5. 用openId获取用户信息