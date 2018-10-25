package com.example.demo;

import com.example.demo.ActiveMqDemo.AyMoodProducer;
import com.example.demo.model.AyMood;
import com.example.demo.model.AyUser;
import com.example.demo.service.AyMoodService;
import com.example.demo.service.AyUserService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
//引用入口类的配置
@SpringBootTest
public class DemoApplicationTests {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
	private AyUserService ayUserService;

    @Resource
	private RedisTemplate redisTemplate;

    @Resource
	private StringRedisTemplate stringRedisTemplate;

    @Resource
	private AyMoodService ayMoodService;

    @Resource
	private AyMoodProducer ayMoodProducer;


	Logger logger= LogManager.getLogger(this.getClass());


	//测试项目启动
	@Test
	public void contextLoads() {
		System.out.println("111");
	}

	//测试连接mysql数据库
	@Test
	public void mySqlTest(){
		String sql="select * from ay_user";
		List<AyUser> list= (List<AyUser>)jdbcTemplate.query(sql, new RowMapper<AyUser>() {
			@Override
			public AyUser mapRow(ResultSet rs, int i) throws SQLException {
				AyUser ayUser = new AyUser();
				ayUser.setId(rs.getString("id"));
				ayUser.setName(rs.getString("name"));
				ayUser.setPassword(rs.getString("password"));
				return ayUser;
			}
		});
		System.out.println("查询成功");
		for(AyUser ayUser:list){
			System.out.println("id:"+ayUser.getId()+"name:"+ayUser.getName()+"password:"+ayUser.getPassword());
		}
	}

	//测试集成spring Data JpaRepository
	@Test
	public void testRepository(){

		List<AyUser> list=ayUserService.findAll();
		System.out.println("findAll()"+list.size());

		//通过name查询数据
		List<AyUser> userList=ayUserService.findByName("毅");
		System.out.println("findByName()"+userList.size());

		//断言如果正确走success
		Assert.assertTrue("data wrong~",userList.get(0).getName().equals("毅"));

		//通过name模糊查询数据
		List<AyUser> userList1=ayUserService.findByNameLike("%毅%");
		System.out.println("findByNameLike()"+userList1.size());

		Assert.assertTrue("data wrong1~",userList.get(0).getName().equals("毅"));

		//通过Id列表查询数据
		List<String> ids=new ArrayList<String>();
		ids.add("1");
		ids.add("2");
		List<AyUser> userList2=ayUserService.findByNameIdIn(ids);
		System.out.println("findByNameIdIn()"+userList2.size());

		//分页查询数据
		PageRequest pageRequest=new PageRequest(0,10);
		Page<AyUser> userList3=ayUserService.findAll(pageRequest);

		System.out.println("Page findAll()"+userList3.getTotalPages()+":"+userList3.getTotalElements());

		//新增数据
		AyUser ayUser=new AyUser();
		ayUser.setId("4");
		ayUser.setName("TEST");
		ayUser.setPassword("1234567");
		ayUserService.save(ayUser);

		//删除数据
		ayUserService.delete("4");

	}

	//测试事物方法
	@Test
	public void testTransaction(){

		AyUser ayUser=new AyUser();
		ayUser.setId("5");
		ayUser.setName("仔");
		ayUser.setPassword("123456");
		ayUserService.save(ayUser);

	}

	//测试redis方法
	@Test
	public void testRedis(){

        //增key: name ,value: ay
        redisTemplate.opsForValue().set("name","al");
        //查询
        String name=(String)redisTemplate.opsForValue().get("name");
        System.out.println(name);
        //删除
        redisTemplate.delete("name");

        //增
        stringRedisTemplate.opsForValue().set("name","ay");
        name=stringRedisTemplate.opsForValue().get("name");
        System.out.println(name);

	}

	@Test
	public void testFindById(){

		Long redisUserSize=0L;
		//查询id=1的数据，该数据存在于Redis缓存中
		AyUser ayUser=ayUserService.findById("1");
		redisUserSize=redisTemplate.opsForList().size("ALL_USER_LIST");
		System.out.println("目前缓存中的用户数量为："+redisUserSize);
		System.out.println(1111233);

		//查询id=2的数据，该数据存在于Redis缓存中
		AyUser ayUser1=ayUserService.findById("2");
		redisUserSize=redisTemplate.opsForList().size("ALL_USER_LIST");
		System.out.println("目前缓存中的用户数量为："+redisUserSize);

		//查询id=4的数据，不存在于Redis中，存在于数据库中，所以吧数据库中查到的数据更新到缓存中
		AyUser ayUser3=ayUserService.findById("4");
		redisUserSize=redisTemplate.opsForList().size("ALL_USER_LIST");
		System.out.println("目前缓存中的用户数量为："+redisUserSize);

	}

	@Test
	public void testLog4j(){

	ayUserService.delete("4");
	logger.info("delete success!!!");

	}

	@Test
	public void testMybatis(){

		AyUser ayUser=ayUserService.findByNameAndPassword("毅","123456");
		logger.info("ssssss:"+ayUser.getId()+ayUser.getName());
	}



	@Test
	public void testAyMood(){

		AyMood ayMood=new AyMood();
		ayMood.setId("1");
		//用户阿毅id为1
		ayMood.setUserId("1");
		ayMood.setPraiseNum(0);
		//说说内容
		ayMood.setContent("这是我的第一条说说！！！");
		ayMood.setPublishTime(new Date());
		//往数据库保存一条数据，代码用户阿毅发表了一条说说
		AyMood mood=ayMoodService.save(ayMood);

	}

	@Test
	public void testActiveMQ(){

		Destination destination=new ActiveMQQueue("ay.queue");
		ayMoodProducer.sendMessage(destination,"hello,mq~~~");
	}


	//测试异步队列
	@Test
	public void testActiveMQAsynSave(){

		AyMood ayMood=new AyMood();
		ayMood.setId("2");
		ayMood.setUserId("2");
		ayMood.setPraiseNum(0);
		ayMood.setContent("这是我第一条说说~~~~~");
		ayMood.setPublishTime(new Date());
		String msg=ayMoodService.asynSave(ayMood);
		System.out.println("异步发表说说："+msg);
	}

	//测试异步调用
	@Test
	public void testAsync(){
		long startTime = System.currentTimeMillis();
		System.out.println("第一次查询所有用户！");
		List<AyUser> ayUserList = ayUserService.findAll();
		System.out.println("第二次查询所有用户！");
		List<AyUser> ayUserList2 = ayUserService.findAll();
		System.out.println("第三次查询所有用户！");
		List<AyUser> ayUserList3 = ayUserService.findAll();
		long endTime = System.currentTimeMillis();
		System.out.println("总共消耗：" + (endTime - startTime) + "毫秒");
	}

	//测试异步调用2
	@Test
	public void testAsync2()throws Exception{
		long startTime = System.currentTimeMillis();
		System.out.println("第一次查询所有用户！");
		Future<List<AyUser>> ayUserList = ayUserService.findAsynAll();
		System.out.println("第二次查询所有用户！");
		Future<List<AyUser>> ayUserList2 = ayUserService.findAsynAll();
		System.out.println("第三次查询所有用户！");
		Future<List<AyUser>> ayUserList3 = ayUserService.findAsynAll();
		//isDone()判断是否计算完
		while (true){
			if(ayUserList.isDone() && ayUserList2.isDone() && ayUserList3.isDone()){
				break;
			}else {
				Thread.sleep(10);
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("总共消耗1：" + (endTime - startTime) + "毫秒");
	}


}
