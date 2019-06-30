Scala backend of "0-1" team's project on HanabiHack Moscow June 2019

## API documentation:
### User
* Get all users: GET /users
* Get user by id: POST /users/?id
* Get user by github link: POST /users/?link
* Create new user: POST /users/?createdLink
* Auth user: POST /auth/?github_link
* Update github link: POST updateinfo/?id&github_link
* Update vk link: POST updateinfo/?id&vk_link

### Handle main operations
* Handle VK analytics: POST handleVK/?id
* Handle Github analytics: POST handleGithub/?id

### Handle GithubAnalyticsStat
* Get all stats: GET /githubstats
* Get stat by id: POST /githubstats/?id
* Get stat by user: POST /githubstats/?id

### Handle VKAnalyticsStat
* Get all stats: GET /vkstats
* Get stat by id: POST /vkstats/?id
* Get stat by user: POST /vkstats/?id
