# 박달 모드 개발 지침 요약

## 📚 구성 문서

- [Trait 시스템](./docs/trait.md)
- [입력 및 서버 동기화 구조](./docs/network_sync.md)
- [클라이언트 애니메이션 처리](./docs/animation.md)
- [코딩 스타일 가이드라인](./docs/rules.md)

## 🧠 기본 규칙 (GPT 참고용)

- 모든 시스템은 Trait 기반으로 관리됩니다.
- 키 입력 감지는 클라이언트 → 상태 변화 시 서버에 통보 → 서버 검증 후 브로드캐스트
- 모든 Trait 값은 `TraitData`를 통해 저장 및 로드됨.
- 애니메이션은 클라이언트 전용, 상태는 서버가 판단.
- 무조건 멀티 환경 안정성 최우선.

## ✅ GPT에게 요청 시

> 예: climbing 상태 Trait 처리 구현해줘  
> → GPT는 `docs/trait.md`, `network_sync.md` 참조하여 설계
