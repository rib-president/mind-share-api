import com.mindshare.app.platform.api.PlatformApiApplication;
import com.mindshare.app.platform.api.repository.ArticleRepository;
import com.mindshare.app.platform.api.repository.CategoryRepository;
import com.mindshare.app.platform.api.repository.UserRepository;
import com.mindshare.app.platform.api.repository.UserTypeRepository;
import com.mindshare.app.platform.api.service.board.ArticleService;
import com.mindshare.app.platform.api.service.user.AuthService;
import com.mindshare.domain.board.entity.Article;
import com.mindshare.domain.user.entity.User;
import io.client.core.provider.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(classes = PlatformApiApplication.class)
public class PlatformApiApplicationTests {
  @Autowired
  private ArticleService articleService;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ArticleRepository articleRepository;
  @Autowired
  private UserTypeRepository userTypeRepository;
  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private JwtProvider jwtProvider;
  @Autowired
  private AuthService authService;

  private static BigInteger articleId;

  @BeforeEach
  void setup() {
    User user = User.builder()
        .userType(userTypeRepository.findByName("STUDENT").orElseThrow())
        .name("안녕")
        .emailAddress("hihi@hihi.com")
        .phoneNumber("01011111111")
        .password("1234")
        .build();

    userRepository.save(user);

    Article article = Article.builder()
        .title("제목")
        .content("내용")
        .category(categoryRepository.findByLabel("예술").orElseThrow())
        .author(user)
        .authorName(user.getName())
        .build();
    articleRepository.save(article);
    articleId = article.getArticleId();

  }

  @Test
  public void 조회수_분산락_동시성_테스트() throws InterruptedException {
    int numberOfThreads = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads);

    for (int i = 0; i < numberOfThreads; i++) {
      executorService.submit(() -> {
        try {
          articleService.getOne(articleId);
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();

    Article result = articleRepository.findById(articleId)
        .orElseThrow();
    System.out.println("총 조회수: " + result.getViewCount());  // 100
  }
}
