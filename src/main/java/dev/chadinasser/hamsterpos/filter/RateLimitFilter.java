package dev.chadinasser.hamsterpos.filter;

// TODO: Enable rate limiting later if needed
//@Component
//public class RateLimitFilter extends OncePerRequestFilter {
//    private final Bucket bucket;
//
//    public RateLimitFilter(Bucket bucket) {
//        this.bucket = bucket;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        if (bucket.tryConsume(1)) {
//            filterChain.doFilter(request, response);
//        } else {
//            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//        }
//    }
//}
