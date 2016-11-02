[消息定义]
 1. 注册消息 code(byte 1) -> parkId (String)->parkName(String)->freeCount(int) 每60秒发一次，也用于心跳
 2. 查询停车信息 code(byte 2)->carNumber(String)
 3. 停车信息响应 code(byte 3)->startTime(String)->consumedTime(String)->price(float)->carNumber(String)
 4. 空余车位 code(byte 4)
 5. 空余车位响应: code (byte 5)->freeNumber(int)

 [帐号信息]
 AppID(应用ID)wxc2b584fcefc93605
 AppSecret(应用密钥)5bf062b4754ac5f05783a8804977958e
 微信支付商户号
 1360170902
 商户平台登录帐号
 1360170902@1360170902
 商户平台登录密码
 741030
 申请对应的公众号
 汇九川（）

 key: njfzcrjyxgs201606292120101234567
 reserved: njfzc.com

 AES: 7Dk9gfs1pp+zy+3R1tGLvg==

支付宝公钥
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB

 ----private key RSA pc8-----
-----BEGIN PRIVATE KEY-----
MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALrtH2L7OgX9cruV
zqvRp3ASYF0KDBBt34Dksn/3wgLHqy3doh6/aKDKMmu0aHzpwk03ymg5fLuA7Brf
EZPe0wdpIyy/K9IVQ9WcwutKXeJ/vfAHR6YImLsEHeF9xKtS9DvDW0XirJ9JUL2d
nrDvLqLF27A6PCHW1Eyv9i5cRxJrAgMBAAECgYBDOtOmEvtsehqQ0zGQ5IfXLBTS
gbGTGyFex2JX2jFCNLQhe+w6KNeltPNrf2hxPNXwVdeLotl0ysqgY4h76ZF2JHVT
ivePKejgeRn0Gb8tlr+fKEkF+aVBUI9dzXm3ITkCpseu8UtpCq/ZfoATeCkhCKTZ
w8G6alxAiQIbHtV2GQJBAODGLh8I3gp6v2H5hwN5+5N+Et1pGyTb34vqbA7Et+DP
HNCgO3geNqK42dCwTJsycYs+vsmH76NVjdsr84QFhk0CQQDU5O2t80tvFaa+ZK1i
XXPXstOYoDWlkWdWQ7PaM4AovD1Ob4SGROvC5dMzj0MwbmQ713IiWUSAKkVv6zDl
m8eXAkAwb0P8+AIwq+aVjBllzGFDlQUnpMBuntp64dbLD+S3kqmY4w4tggUv5zk4
WOUJBEOnR6wA7UPcJFFfCwd8SVx5AkBYrtRjdcmiiH5hKUcghpVO8Os71OEEC0Hk
qWcuKe3lCiTvm3y1AdjD40DLZY4zioiudNzSeiUSzokGSg6gVvgZAkBJAUGxNue1
q3lh0KQK4LWRMTvIYUL0PRAxoLfn7qvPUJ/aUBwsDqFEKkiB4bU1e84nWauzYzwv
ENSWKfreNEl/
-----END PRIVATE KEY-----

---------------public key------------
-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC67R9i+zoF/XK7lc6r0adwEmBd
CgwQbd+A5LJ/98ICx6st3aIev2igyjJrtGh86cJNN8poOXy7gOwa3xGT3tMHaSMs
vyvSFUPVnMLrSl3if73wB0emCJi7BB3hfcSrUvQ7w1tF4qyfSVC9nZ6w7y6ixduw
Ojwh1tRMr/YuXEcSawIDAQAB
-----END PUBLIC KEY-----

----private key for php .net-----------------------------
-----BEGIN RSA PRIVATE KEY-----
MIICWwIBAAKBgQC67R9i+zoF/XK7lc6r0adwEmBdCgwQbd+A5LJ/98ICx6st3aIe
v2igyjJrtGh86cJNN8poOXy7gOwa3xGT3tMHaSMsvyvSFUPVnMLrSl3if73wB0em
CJi7BB3hfcSrUvQ7w1tF4qyfSVC9nZ6w7y6ixduwOjwh1tRMr/YuXEcSawIDAQAB
AoGAQzrTphL7bHoakNMxkOSH1ywU0oGxkxshXsdiV9oxQjS0IXvsOijXpbTza39o
cTzV8FXXi6LZdMrKoGOIe+mRdiR1U4r3jyno4HkZ9Bm/LZa/nyhJBfmlQVCPXc15
tyE5AqbHrvFLaQqv2X6AE3gpIQik2cPBumpcQIkCGx7VdhkCQQDgxi4fCN4Ker9h
+YcDefuTfhLdaRsk29+L6mwOxLfgzxzQoDt4HjaiuNnQsEybMnGLPr7Jh++jVY3b
K/OEBYZNAkEA1OTtrfNLbxWmvmStYl1z17LTmKA1pZFnVkOz2jOAKLw9Tm+EhkTr
wuXTM49DMG5kO9dyIllEgCpFb+sw5ZvHlwJAMG9D/PgCMKvmlYwZZcxhQ5UFJ6TA
bp7aeuHWyw/kt5KpmOMOLYIFL+c5OFjlCQRDp0esAO1D3CRRXwsHfElceQJAWK7U
Y3XJooh+YSlHIIaVTvDrO9ThBAtB5KlnLint5Qok75t8tQHYw+NAy2WOM4qIrnTc
0nolEs6JBkoOoFb4GQJASQFBsTbntat5YdCkCuC1kTE7yGFC9D0QMaC35+6rz1Cf
2lAcLA6hRCpIgeG1NXvOJ1mrs2M8LxDUlin63jRJfw==
-----END RSA PRIVATE KEY-----



[public]
huxuechuan@163.com
fzcrjyxgs5158
fzcrjyxgs5158
[aliyun]
njfzcrjyxgs
fzcrjyxgs5158

402493

[ICP代备案管理系统]
kkjattuu@163.com
fzcrjyxgs58
913201153026309482

64265871@qq.com
s1234567689