# [프로그라피 10기 AOS 과제] 하상범 - 4863

Unsplash API를 활용한 이미지 갤러리 애플리케이션으로, Jetpack Compose를 이용한 최신 안드로이드 개발 기술을 적용했습니다. 
사용자는 최신 이미지를 탐색하고, 마음에 드는 이미지를 북마크할 수 있으며, 랜덤 이미지를 카드 형태로 탐색할 수 있습니다.

## 사용한 기술 스택
- **언어**: Kotlin
- **UI 프레임워크**: Jetpack Compose
- **아키텍처**: MVVM, Repository 패턴
- **비동기 처리**: Kotlin Coroutines, Flow
- **네트워킹**: Retrofit2, OkHttp3
- **이미지 로딩**: Coil
- **로컬 데이터베이스**: Room (북마크 저장)
- **네비게이션**: Compose, Navigation

## 화면별 주요 기능

### 1. 메인 화면 (첫 번째 탭)
![메인 화면 스크린샷 삽입 예정...]()

**1.1 북마크 섹션**
   - Local DB(Room)에 저장된 북마크 이미지 표시
   - 저장된 북마크가 없는 경우 북마크 섹션 자체가 표시되지 않음.
   - 북마크 목록은 가로 스크롤로 볼 수 있음.

**1.2 최신 이미지**
   - 커스텀 StaggeredGrid 레이아웃으로 이미지 높이에 따른 동적 그리드 구현
   - 이미지 로딩 중 로딩 인디케이터 표시
   - 각 이미지에 제목과 설명 오버레이 표시
   - 무한 스크롤 구현 (페이지네이션)

**1.3 로딩 상태** 
   - 초기 로딩 시 SkeletonView 표시
   - 추가 로딩 시 하단에 로딩 인디케이터 표시
   - 로딩 애니메이션 

### 2. 랜덤 포토 화면 (두 번째 탭)
![랜덤 포토 스크린샷 삽입 예정...]()

**2.1 카드 UI** 
   - 사진 카드 좌우 스와이프 기능 
   - 오른쪽으로 스와이프하면 자동 북마크 저장 
   - 하단 버튼으로 북마크, 정보 확인, 건너뛰기 기능 

### 3. 포토 디테일 화면
![상세 화면 스크린샷 삽입 예정...]()

**3.1 이미지 상세 정보**
   - 다이얼로그 형태의 전체화면 상세보기
   - 북마크 토글 기능 (활성화 시 영구 저장)
   - 사용자 정보 및 태그 표시
  
## 화면별 개선 필요 항목 정리 예정...
