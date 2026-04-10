# HW Testing - Concept Questions

## Testing related

### 1. Unit Testing
- Definition: Testing the smallest testable unit (function/method/class) and it should NOT rely on real external dependencies.
- Focus: Correctness of individual components.
- Example: In `CommentServiceImpl`, mock repository methods with Mockito and verify each service method behavior.
- Comparison: More isolated than Integration Testing; uses mocks instead of real DB/services.

### 2. Functional Testing
- Definition: Testing a function of one module or a complete application from the user's perspective.
- Focus: Whether business requirements/features are fulfilled.
- Example: Verify comment create/read/delete flow behaves correctly from API/user behavior perspective.
- Comparison: Unlike Unit Testing, it is feature behavior oriented and less implementation-oriented.

### 3. Integration Testing
- Definition: Testing interactions between multiple components/modules.
- Focus: Whether collaboration between components is correct.
- Example: `CommentService` + `CommentRepository` + database working together with real dependencies.
- Comparison: Unit test is white-box and mocked; integration test is black-box and real dependency based.

### 4. Regression Testing
- Definition: After code/environment changes, re-run existing test suites to ensure no new defects are introduced.
- Focus: Existing functionality remains correct after changes.
- Example: In CI/CD, rerun previous tests after adding a new comment-related feature.
- Comparison: Regression is about "stability after change", not a separate test level.

### 5. Smoke Testing
- Definition: Build Verification Test (BVT) in CI/CD to validate that a newly deployed executable supports basic functions.
- Focus: Decide whether pipeline should continue to deeper testing stages.
- Example: Deploy build, verify app starts and core APIs respond; if failed, stop pipeline immediately.
- Comparison: Smoke is shallow and fast; Regression is broader and deeper.

### 6. Performance Testing
- Definition: Testing response time and efficiency under a specific expected workload (for example QPS).
- Focus: Performance target under normal load.
- Example: Under expected QPS, ensure API latency target (for example around 200ms) is met.
- Comparison: Performance validates expected load; Stress validates beyond expected load.

### 7. Stress Testing
- Definition: Testing system capacity under workload/data volume beyond normal level.
- Focus: System behavior under high pressure and upper-limit capacity.
- Example: Push traffic near/above high QPS level (for example 2k QPS) and observe stability.
- Comparison: Stress is overload/capacity oriented; Performance is normal workload oriented.

### 8. A/B Testing
- Note: This item is in homework questions but is not explicitly defined in `48-Testing.pdf`.
- Definition: Split users into groups A and B, compare business metrics for two variants.
- Focus: Business outcome optimization (not code correctness).
- Example: Compare two comment ranking strategies by engagement metrics.

### 9. End-to-End Testing
- Definition: Validate full end-to-end functionality across the whole system flow.
- Focus: Whether complete user journey works as expected.
- Example: QA validates login -> post comment -> view comment -> delete comment in one full flow.
- Comparison: Broader scope than unit/integration tests and usually executed by QA.

### 10. User Acceptance Testing (UAT)
- Definition: End users/business users test software under real/actual conditions.
- Focus: Whether software meets user needs and expectations.
- Example: Business users validate comment moderation workflow before release sign-off.
- Comparison: UAT is user/business validation; functional/integration tests are engineering/QA validation.

## Environment related

### 1. Development
- Definition: Team/shared development environment for integration and ongoing feature development.
- Goal: Support daily coding and internal testing.
- Characteristics: Frequent updates and active collaboration.

### 2. QA (Quality Assurance)
- Definition: Environment where QA engineers run end-to-end, functional, and regression validations.
- Goal: Defect discovery and quality gate before staging.
- Characteristics: Test data and test scenarios are more standardized than development.

### 3. Pre-prod / Staging
- Definition: Staging environment that is ready-for-production and used as final rehearsal.
- Goal: Final validation before production release.
- Characteristics: Should be highly similar to production configuration and deployment flow.

### 4. Production
- Definition: Live environment used by real users.
- Goal: Reliability and correct business service in real traffic.
- Characteristics: Strict change control and high stability requirement.

## Quick comparison (environment flow)
- Typical lecture flow: `temporary development -> development -> qa -> staging -> production`
- Your homework list maps to: `development -> qa -> pre-prod/staging -> production`
