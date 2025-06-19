# 박달 모드 Trait 시스템 개요

## 박달 Trait 개요

Trait은 모든 플레이어 상태와 시스템을 Trait 기반으로 관리합니다. Identifier("trait")로 저장되며, 클라이언트와 서버가 TraitData를 거친 상태 동기화 처리를 수행합니다.

## 박달 Trait 읽기 방식

```java
TraitData trait = TraitBridge.get(player).get(Trait.loc.toString());
boolean isClimbing = trait.getBoolean(Trait.Bool.IS_CLIMBING.name());
```

- `TraitBridge.get(player)` → TraitComponent에 접근
- `.get(Trait.loc.toString())` → "trait" Identifier를 기준으로 TraitData 가져\uc옴
- `.getBoolean(...)`, `.getInt(...)`, `.getDouble(...)` 등 Enum 기반의 함수로 값 찾기

## Trait 추가 시 지침

1. `Trait.java` 내부의 Enum(`Bool`, `Int`, `Double`, `Str`) 중 해당되는 것에 추가
2. `Trait.create()` 메소드에서 처음 값 설정
3. 필요에 따라 `.setBoolean(...)`, `.setInt(...)` 등의 메소드 사용

> ⚠️ Trait이 추가되면 GPT에게 통지해 최신화를 요청해주세요.

```java
public enum Bool {
    ...
    IS_LEDGE_GRABBING
}

// 초기값 설정
trait.setBoolean(Trait.Bool.IS_LEDGE_GRABBING.name(), false);
```

### 사용 경로 명시 (선택)

Trait 결정이 힘들고 분리가 다양할 경우, 해당 Trait을 사용하는 건설 클래스 경로를 명시해 보고를 줄 수 있습니다:

```java
// 참조 경로
common/src/main/java/kr/cantua/movement/climb/LedgeGrabHandler.java
```

GPT는 경로의 코드를 참고하여 해당 Trait의 접견, 감지, 최종 처리 구조를 정확히 해석할 수 있습니다.

## 설계 원칙

- Trait Enum 이름은 **대문자 + 스네이크 케이스**
- 모든 Trait 값은 **NBT 직렬화**로 저장됨
- **Fabric**: Cardinal Components 사용
- **NeoForge**: Custom Capability 구현

## 참고

이 Trait 구조는 [SmartMoving](https://github.com/makamys/SmartMoving) 모드의 상태 처리에서 영감을 받아
멀티플레이어 환경에서 효율적으로 상태를 동기화할 수 있도록 설계되었습니다.
