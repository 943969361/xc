<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title>
</head>
<body>
Hello ${name}!

<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>
<#list stus as stu>
<tr>
    <td>${stu_index + 1}</td>
    <td>${stu.name}</td>
    <td>${stu.age}</td>
    <td>${stu.money}</td>
</tr>
</#list>
</table>
输出stu1的学生信息：<br/>
    姓名：${stuMap['stu1'].name},
    年龄：${stuMap['stu1'].age},
    ${stuMap.stu2.name},
    ${stuMap.stu2.age},
</br>
    <#list stuMap?keys as k>
        姓名：${stuMap[k].name},
        年龄：${stuMap[k].age},</br>
    </#list>
</body>
</html>
