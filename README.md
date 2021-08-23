# :pushpin: portfolio

</br>

## 1. 제작 기간 & 참여 인원
- 2021년 8월 01일 ~ 8월 15일
- 개인 프로젝트

</br>

## 2. 사용 기술
#### `Back-end`
  - Java 8
  - Spring Boot 2.5.3
  - Gradle
  - Spring Data JPA
  - H2 Database
  
#### `Front-end`
  - Thymeleaf

</br>

## 3. ERD 설계
![](https://github.com/deokgoni/portfolio/blob/master/src/main/resources/static/image/erd01.png)

</br>

## 4. 핵심 기능
이 서비스의 핵심 기능은 도서 관리자 화면 기능입니다.  
로그인된 관리자만 접속 가능하고 회원, 도서, 주문에 관련된 등록 및 내역 확인을 제공하며
아래의 기능의 흐름을 보면, 서비스가 어떻게 동작하는지 알 수 있습니다.  
</br>

### 4.1. 전체 흐름
![](https://github.com/deokgoni/portfolio/blob/master/src/main/resources/static/image/flow.png)
- 서비스, 리포지토리 계층을 개발하고, 테스트 케이스를 작성해서 검증, 마지막에 웹 계층 적용합니다.
</br>

### 4.2. 사용자 요청
![](https://github.com/deokgoni/portfolio/blob/master/src/main/resources/static/image/flow_view01.png)
![](https://github.com/deokgoni/portfolio/blob/master/src/main/resources/static/image/flow_view02.png)

- **th:errors로 오류 체크** :pushpin: [코드 확인](https://github.com/deokgoni/portfolio/blob/master/src/main/resources/templates/login/loginForm.html)
  - 타임리프를 사용하여 스프링의 BindingResult 를 활용해서 편리하게 검증 오류를 구현합니다.
  - th:errors, th:errorclass : th:field 에서 지정한 필드에 오류가 있으면, 에러 메세지를 띄웁니다.
</br>

### 4.3. Controller

![](https://github.com/deokgoni/portfolio/blob/master/src/main/resources/static/image/flow_controller.png)

- **요청 처리** :pushpin: [코드 확인](https://github.com/deokgoni/portfolio/blob/master/src/main/java/com/gon/webservice/controller/ItemController.java)
  - Controller에서는 요청을 화면단에서 넘어온 요청을 받고, Service 계층에 로직 처리를 위임합니다.
  - Service 계층에서 넘어온 로직 처리 결과(메세지)를 화면단에 응답해줍니다.
</br>

### 4.4. Service

![](https://github.com/deokgoni/portfolio/blob/master/src/main/resources/static/image/flow_Service01.png)

- **트랜잭션 처리** :pushpin: [코드 확인](https://github.com/deokgoni/portfolio/blob/master/src/main/java/com/gon/webservice/service/OrderService.java)
  - Service에서는 비즈니스 로직과 트랜잭션 처리를 위임합니다.
  - @Transactional(readOnly=true) : 데이터의 변경이 없는 읽기 전용 메서드에 사용하여 영속성 컨텍스트를 flush() 하지 않
                                    으므로 약간의 성능 향상을 제공합니다.

- **비지니스 로직 테스트** :pushpin: [코드 확인](https://github.com/deokgoni/portfolio/blob/master/src/test/java/com/gon/webservice/service/OrderServiceTest.java)
  - 테스트 요구사항을 정하고 상품주문이 정상 동작하는지 확인하는 테스트입니다. 
  - Given 절에서 테스트를 위한 회원과 상품을 만들고 When 절에서 실제 상품을 주문하고 Then 절에서 주문 가격이 올바른지, 주문 후 재고 수량이 정확히 줄었는지 검증합니다.
</br>

### 4.5. Repository

![](https://github.com/deokgoni/portfolio/blob/master/src/main/resources/static/image/flow_repository02.png)

- **컨텐츠 저장** :pushpin: [코드 확인](https://github.com/deokgoni/portfolio/blob/master/src/main/java/com/gon/webservice/repository/MemberRepository.java)
  - Entity에 의해 생성된 DB에 접근하는 메서드를 구현합니다.
  - DB에서 조회된 정보는 다시 Repository - Service - Controller를 거쳐 화면단에 송출됩니다.
</br>

## 5. 핵심 트러블 슈팅
</br>

### 5.1. 검증 로직 추가 문제
- 단순히 타임리프를 통해서 검증처리를 진행하려고 하니 코드도 길어지고 가독성이 떨어진다는 것을 알게 되었습니다.

<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

~~~html
<input type="text" id="email" th:field="*{email}" 
       th:class="${errors?.containsKey('email')} ? 'form-control field-error' : 'form-control'"
       class="form-control" placeholder="이메일을 입력하세요">
 <div class="field-error" th:if="${errors?.containsKey('email')}" th:text="${errors['email']}">
 이메일 오류
 </div>
~~~
  
</div>
</details>

- 스프링의 BindingResult와 타임리프의 th:field와 th:errors를 이용하면 검증처리가 깔끔해 진다는 점을 찾고 보완했습니다.

<details>
<summary><b>보완 코드</b></summary>
<div markdown="1">

~~~html
<input type="email" th:field="*{email}" 
       th:errorclass="fieldError" 
       class="form-control" placeholder="이메일를 입력하세요">
<p class="fieldError" th:errors="*{email}">이메일 오류</p>
~~~
  
</div>
</details>

### 5.2. 로그인 인증 접근 문제
- 로그인 되지 않은 사용자는 어드민 페이지에 URL을 통해 접근하지 못하게 하기 위해 서블릿 필터를 구현했습니다. 
- 기본 로그인이나 로그아웃 회원가입을 제외하고 비인증 유저는 접근하지 못하게 하기 위해서는 Controller에서 session 확인을 진행하는 것은
  중복된 코드가 계속 발생하고 예기치 않은 에러를 발생시킬 수 있기에 서블릿 필터로 진행하였습니다.
  
 <details>
<summary><b>필터 코드</b></summary>
<div markdown="1">

~~~java
  
  /**
     * LoginFilter
     */
  private static final String[] Blocklist = {"/", "/members/new", "/login", "/logout", "/css/**", "/*.ico", "/error", "/js/**"};
  
 @Override
 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
   
   HttpServletRequest httpRequest = (HttpServletRequest) request;
   String requestURI = httpRequest.getRequestURI();
   HttpServletResponse httpResponse = (HttpServletResponse) response;
 
  try { 
       if (PatternMatchUtils.simpleMatch(Blocklist, requestURI)) {
       HttpSession session = httpRequest.getSession(false);
       
      //인증되지 않은 사용자
      if (session == null || session.getAttribute("sessionName") == null) {
         
         //로그인으로 redirect
         httpResponse.sendRedirect("/login");
         return; 
         }
       }
       chain.doFilter(request, response);
   } catch (Exception e) {
       throw e; 
   } 
  
  /**
     * WebConfig
     */
  @Configuration
  public class WebConfig {
    @Bean
    public FilterRegistrationBean loginCheckFilter() {
       FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
       filterRegistrationBean.setFilter(new LoginFilter());
       filterRegistrationBean.setOrder(1);
       filterRegistrationBean.addUrlPatterns("/*");
       return filterRegistrationBean;
    }
  }
~~~
  
</div>
</details>

  - 필터의 개선 방향으로 인터셉터도 구현했으며 인터셉터 사용 결과,  preHandle에서 HttpServletResponse를 받기에 필터처럼 
    형변환을 진행할 필요도 없고 코드의 가독성과 편의성이 상당 부분 개선되었다는 것을 알 수 있습니다. 
  
<details>
<summary><b>인터셉터 코드</b></summary>
<div markdown="1">

~~~java
  
  /**
     * LoginInterceptor
     */
  @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //세션 생성
        HttpSession session = request.getSession(false);
        //인증되지 않은 사용자
        if (session == null || session.getAttribute("sessionName") == null) {
            //로그인으로 redirect
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
  
    /**
     * WebConfig
     */
  @Configuration
  public class WebConfig implements WebMvcConfigurer {
   @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/new", "/login", "/logout", "/css/**", "/*.ico", "/error", "/js/**");
    }
  }
~~~
  
</div>
</details>
