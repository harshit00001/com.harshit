# Parking Lot LLD (Java)

Interactive Low Level Design project for interview preparation.

**Full design guide (requirements, flow, UML-style classes, patterns, concurrency):** [PARKING_LOT_DESIGN.md](./PARKING_LOT_DESIGN.md)

## Run

```bash
cd com.harshit/parking-lot-lld
mvn compile exec:java
```

Or run `ParkingLotApplication` from your IDE.

## Demo flow

1. **Park vehicle** - choose type (Motorcycle/Car/Truck) and license plate
2. **Unpark vehicle** - enter ticket id from entry
3. **Show status** - see all spots and availability
4. **Active tickets** - list vehicles currently inside
5. **Change pricing** - switch Hourly vs Flat (Strategy pattern demo)

## Design patterns used

| Pattern | Class |
|---------|-------|
| Singleton | `ParkingLot` |
| Factory | `VehicleFactory` |
| Strategy (pricing) | `PricingStrategy`, `HourlyPricingStrategy`, `FlatPricingStrategy` |
| Strategy (spot pick) | `SpotAssignmentStrategy`, `FirstAvailableSpotAssignmentStrategy` |
| Observer | `ParkingObserver`, `DisplayBoard` |

## Package structure

```
com.harshit.parkinglot
├── ParkingLotApplication   # interactive menu
├── enums                   # VehicleType, SpotType, SpotStatus, PaymentStatus
├── model                   # Vehicle, Spot, Floor, Ticket, Payment
├── factory                 # VehicleFactory
├── singleton               # ParkingLot
├── strategy                # pricing + spot assignment
├── gate                    # EntryGate, ExitGate
├── observer                # DisplayBoard
├── util                    # SpotCompatibility rules
└── exception               # ParkingFullException, InvalidTicketException
```

## Sample test input

```
1        -> Park
2        -> Car
MH12AB1234
4        -> Show active tickets (copy ticket id)
2        -> Unpark
<TICKET> -> paste ticket id
6        -> Exit app
```
