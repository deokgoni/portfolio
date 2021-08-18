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
                                    으므로 약간의 성능 향상을 제공한다.

- **비지니스 로직 테스트** :pushpin: [코드 확인](https://github.com/deokgoni/portfolio/blob/master/src/test/java/com/gon/webservice/service/OrderServiceTest.java)
  - 테스트 요구사항을 정하고 상품주문이 정상 동작하는지 확인하는 테스트입니다. 
  - Given 절에서 테스트를 위한 회원과 상품을 만들고 When 절에서 실제 상품을 주문하고 Then 절에서 주문 가격이 올바른지, 주문 후 재고 수량이 정확히 줄     었는지 검증합니다.
</br>

### 4.5. Repository

![](https://github.com/deokgoni/portfolio/blob/master/src/main/resources/static/image/flow_repository02.png)

- **컨텐츠 저장** :pushpin: [코드 확인](https://github.com/deokgoni/portfolio/blob/master/src/main/java/com/gon/webservice/repository/MemberRepository.java)
  - URL 유효성 체크와 파싱이 끝난 정보는 DB에 저장합니다.
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
- 기본 로그인이나 로그아웃 회원가입을 제외하고 미인증 유저는 접근하지 못하게 하기 위해서는 Controller에서 session 확인을 진행하는 것은
  중복된 코드가 계속 발생하고 예기치않은 에러를 발생시킬 수 있기 때문에 필터와 인터셉터로 둘다 적용한 결과, 서블릿 필터보다 편리하고 
  다양한 기능을 지원하기에 인터셉터를 통해 처리를 구현했습니다.
  
 <details>
<summary><b>필터 코드</b></summary>
<div markdown="1">

~~~java
  
   /**
  *  LoginFilter
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
         httpResponse.sendRedirect("/login);
         return; 
         }
       }
       chain.doFilter(request, response);
   } catch (Exception e) {
       throw e; 
   } 
  
   /**
  *  WebConfig
  */
  @Bean
public FilterRegistrationBean loginCheckFilter() {
   FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
   filterRegistrationBean.setFilter(new LoginFilter());
   filterRegistrationBean.setOrder(1);
   filterRegistrationBean.addUrlPatterns("/*");
   return filterRegistrationBean;
}
~~~
  
</div>
</details>

<details>
<summary><b>인터셉터 코드</b></summary>
<div markdown="1">

~~~java
  
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
  *  WebConfig
  */
   @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/new", "/login", "/logout", "/css/**", "/*.ico", "/error", "/js/**");
    }
~~~
  
</div>
</details>

  
  
  
  
- 구현은 했지만 doFilter의 매개변수가 ServletRequest라서 HttpServletResponse를 사용하기 위해서는 형변환을 해줘야 한다는 부분과
  따로 예외처리를 진행해야한다는 점등 단점이 있었습니다.
  
<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

~~~java

  private static final String[] Blocklist = {"/", "/members/add", "/login", "/logout","/css/*"};
  
 @Override
 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
   
   HttpServletRequest httpRequest = (HttpServletRequest) request;
   String requestURI = httpRequest.getRequestURI();
   HttpServletResponse httpResponse = (HttpServletResponse) response;
  
  try { 
       if (PatternMatchUtils.simpleMatch(whitelist, requestURI)) {
       HttpSession session = httpRequest.getSession(false);
       
      //인증되지 않은 사용자
      if (session == null || session.getAttribute("session") == null) {
         
         //로그인으로 redirect
         httpResponse.sendRedirect("/login);
         return; 
         }
       }
       chain.doFilter(request, response);
   } catch (Exception e) {
       throw e; 
   } 
~~~
  
</div>
</details>

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


- 그런데 게시물이 필터링 된 상태에서 무한 스크롤이 동작하면,  
필터링 된 게시물들만 DB에 요청해야 하기 때문에 아래의 **기존 코드** 처럼 각 필터별로 다른 Query를 날려야 했습니다.



- 이 때 카테고리(tag)로 게시물을 필터링 하는 경우,  
각 게시물은 최대 3개까지의 카테고리(tag)를 가질 수 있어 해당 카테고리를 포함하는 모든 게시물을 질의해야 했기 때문에  
- 아래 **개선된 코드**와 같이 QueryDSL을 사용하여 다소 복잡한 Query를 작성하면서도 페이징 처리를 할 수 있었습니다.

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

~~~java
/**
 * 게시물 필터 (Tag Name)
 */
@Override
public Page<Post> findAllByTagName(String tagName, Pageable pageable) {

    QueryResults<Post> results = queryFactory
            .selectFrom(post)
            .innerJoin(postTag)
                .on(post.idx.eq(postTag.post.idx))
            .innerJoin(tag)
                .on(tag.idx.eq(postTag.tag.idx))
            .where(tag.name.eq(tagName))
            .orderBy(post.idx.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
            .fetchResults();

    return new PageImpl<>(results.getResults(), pageable, results.getTotal());
}
~~~

</div>
</details>

</br>

## 6. 그 외 트러블 슈팅
<details>
<summary>npm run dev 실행 오류</summary>
<div markdown="1">

- Webpack-dev-server 버전을 3.0.0으로 다운그레이드로 해결
- `$ npm install —save-dev webpack-dev-server@3.0.0`

</div>
</details>

<details>
<summary>vue-devtools 크롬익스텐션 인식 오류 문제</summary>
<div markdown="1">
  
  - main.js 파일에 `Vue.config.devtools = true` 추가로 해결
  - [https://github.com/vuejs/vue-devtools/issues/190](https://github.com/vuejs/vue-devtools/issues/190)
  
</div>
</details>

<details>
<summary>ElementUI input 박스에서 `v-on:keyup.enter="메소드명"`이 정상 작동 안하는 문제</summary>
<div markdown="1">
  
  - `v-on:keyup.enter.native=""` 와 같이 .native 추가로 해결
  
</div>
</details>

<details>
<summary> Post 목록 출력시에 Member 객체 출력 에러 </summary>
<div markdown="1">
  
  - 에러 메세지(500에러)
    - No serializer found for class org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationConfig.SerializationFeature.FAIL_ON_EMPTY_BEANS)
  - 해결
    - Post 엔티티에 @ManyToOne 연관관계 매핑을 LAZY 옵션에서 기본(EAGER)옵션으로 수정
  
</div>
</details>
    
<details>
<summary> 프로젝트를 git init으로 생성 후 발생하는 npm run dev/build 오류 문제 </summary>
<div markdown="1">
  
  ```jsx
    $ npm run dev
    npm ERR! path C:\Users\integer\IdeaProjects\pilot\package.json
    npm ERR! code ENOENT
    npm ERR! errno -4058
    npm ERR! syscall open
    npm ERR! enoent ENOENT: no such file or directory, open 'C:\Users\integer\IdeaProjects\pilot\package.json'
    npm ERR! enoent This is related to npm not being able to find a file.
    npm ERR! enoent

    npm ERR! A complete log of this run can be found in:
    npm ERR!     C:\Users\integer\AppData\Roaming\npm-cache\_logs\2019-02-25T01_23_19_131Z-debug.log
  ```
  
  - 단순히 npm run dev/build 명령을 입력한 경로가 문제였다.
   
</div>
</details>    

<details>
<summary> 태그 선택후 등록하기 누를 때 `object references an unsaved transient instance - save the transient instance before flushing` 오류</summary>
<div markdown="1">
  
  - Post 엔티티의 @ManyToMany에 영속성 전이(cascade=CascadeType.ALL) 추가
    - JPA에서 Entity를 저장할 때 연관된 모든 Entity는 영속상태여야 한다.
    - CascadeType.PERSIST 옵션으로 부모와 자식 Enitity를 한 번에 영속화할 수 있다.
    - 참고
        - [https://stackoverflow.com/questions/2302802/object-references-an-unsaved-transient-instance-save-the-transient-instance-be/10680218](https://stackoverflow.com/questions/2302802/object-references-an-unsaved-transient-instance-save-the-transient-instance-be/10680218)
   
</div>
</details>    

<details>
<summary> JSON: Infinite recursion (StackOverflowError)</summary>
<div markdown="1">
  
  - @JsonIgnoreProperties 사용으로 해결
    - 참고
        - [http://springquay.blogspot.com/2016/01/new-approach-to-solve-json-recursive.html](http://springquay.blogspot.com/2016/01/new-approach-to-solve-json-recursive.html)
        - [https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue](https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue)
        
</div>
</details>  
    
<details>
<summary> H2 접속문제</summary>
<div markdown="1">
  
  - H2의 JDBC URL이 jdbc:h2:~/test 으로 되어있으면 jdbc:h2:mem:testdb 으로 변경해서 접속해야 한다.
        
</div>
</details> 
    
<details>
<summary> 컨텐츠수정 모달창에서 태그 셀렉트박스 드랍다운이 뒤쪽에 보이는 문제</summary>
<div markdown="1">
  
   - ElementUI의 Global Config에 옵션 추가하면 해결
     - main.js 파일에 `Vue.us(ElementUI, { zIndex: 9999 });` 옵션 추가(9999 이하면 안됌)
   - 참고
     - [https://element.eleme.io/#/en-US/component/quickstart#global-config](https://element.eleme.io/#/en-US/component/quickstart#global-config)
        
</div>
</details> 

<details>
<summary> HTTP delete Request시 개발자도구의 XHR(XMLHttpRequest )에서 delete요청이 2번씩 찍히는 이유</summary>
<div markdown="1">
  
  - When you try to send a XMLHttpRequest to a different domain than the page is hosted, you are violating the same-origin policy. However, this situation became somewhat common, many technics are introduced. CORS is one of them.

        In short, server that you are sending the DELETE request allows cross domain requests. In the process, there should be a **preflight** call and that is the **HTTP OPTION** call.

        So, you are having two responses for the **OPTION** and **DELETE** call.

        see [MDN page for CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS).

    - 출처 : [https://stackoverflow.com/questions/35808655/why-do-i-get-back-2-responses-of-200-and-204-when-using-an-ajax-call-to-delete-o](https://stackoverflow.com/questions/35808655/why-do-i-get-back-2-responses-of-200-and-204-when-using-an-ajax-call-to-delete-o)
        
</div>
</details> 

<details>
<summary> 이미지 파싱 시 og:image 경로가 달라서 제대로 파싱이 안되는 경우</summary>
<div markdown="1">
  
  - UserAgent 설정으로 해결
        - [https://www.javacodeexamples.com/jsoup-set-user-agent-example/760](https://www.javacodeexamples.com/jsoup-set-user-agent-example/760)
        - [http://www.useragentstring.com/](http://www.useragentstring.com/)
        
</div>
</details> 
    
<details>
<summary> 구글 로그인으로 로그인한 사용자의 정보를 가져오는 방법이 스프링 2.0대 버전에서 달라진 것</summary>
<div markdown="1">
  
  - 1.5대 버전에서는 Controller의 인자로 Principal을 넘기면 principal.getName(0에서 바로 꺼내서 쓸 수 있었는데, 2.0대 버전에서는 principal.getName()의 경우 principal 객체.toString()을 반환한다.
    - 1.5대 버전에서 principal을 사용하는 경우
    - 아래와 같이 사용했다면,

    ```jsx
    @RequestMapping("/sso/user")
    @SuppressWarnings("unchecked")
    public Map<String, String> user(Principal principal) {
        if (principal != null) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            Map<String, String> details = new LinkedHashMap<>();
            details = (Map<String, String>) authentication.getDetails();
            logger.info("details = " + details);  // id, email, name, link etc.
            Map<String, String> map = new LinkedHashMap<>();
            map.put("email", details.get("email"));
            return map;
        }
        return null;
    }
    ```

    - 2.0대 버전에서는
    - 아래와 같이 principal 객체의 내용을 꺼내 쓸 수 있다.

    ```jsx
    UsernamePasswordAuthenticationToken token =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder
                            .getContext().getAuthentication();
            Map<String, Object> map = (Map<String, Object>) token.getPrincipal();

            String email = String.valueOf(map.get("email"));
            post.setMember(memberRepository.findByEmail(email));
    ```
        
</div>
</details> 
    
<details>
<summary> 랭킹 동점자 처리 문제</summary>
<div markdown="1">
  
  - PageRequest의 Sort부분에서 properties를 "rankPoint"를 주고 "likeCnt"를 줘서 댓글수보다 좋아요수가 우선순위 갖도록 설정.
  - 좋아요 수도 똑같다면..........
        
</div>
</details> 
    
</br>

## 6. 회고 / 느낀점
>프로젝트 개발 회고 글: https://zuminternet.github.io/ZUM-Pilot-integer/
