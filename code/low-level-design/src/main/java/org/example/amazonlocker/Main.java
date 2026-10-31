package org.example.amazonlocker;

/**
 * @Author: eve
 * @Date: 2026-10-12 12:46
 */
public class Main {
}
/*
Design a locker system like Amazon Locker where delivery drivers can deposit packages and
customers can pick them up using a code.
 */

/*
1. primary capabilities
2. error handling
3. scope boundaries


1. primary capabilities
   - Are there different sized compartments?
   - How does the customer get their code? Do we need to send an SMS or email?

2. error handling
   - Can one customer have multiple packages in the system at once? Are access tokens unique per package?
   - How long do the codes last? What happens to a package if it's never picked up?
   - What if all compartments of a given size are full when a driver tries to deposit?

3. scope boundaries
    - What's in scope for this system? Are we just modeling the whole delivery flow, or just the piece from when the driver
    arrives at the locker until the customer picks up?
 */


// 1. Requirements
/*
Requirements:
1. Carrier deposits a package by specifying size (small, medium, large)
   - System assigns an available compartment of matching size
   - Opens compartment and returns access token, or error if no space
2. Upon successful deposit, an access token is generated and returned
   - One access token per package
3. User retrieves package by entering access token
   - System validates code and opens compartment
   - Throws specific error if code is invalid or expired
4. Access tokens expire after 7 days
   - Expired codes are rejected if used for pickup
   - Package remains in compartment until staff removes it
5. Staff can open all expired compartments to manually handle packages
   - System opens all compartments with expired tokens
   - Staff physically removes packages and returns them to sender
6. Invalid access tokens are rejected with clear error messages
   - Wrong code, already used, or expired - user gets specific feedback

Out of scope:
- How the package gets to the locker (delivery logistics)
- How the access token reaches the customer (SMS/email notification)
- Lockout after failed access token attempts
- UI/rendering layer
- Multiple locker stations
- Payment or pricing
 */

// 2. Entities
/*
Entities:
- Compartments
- Locker
- AccessToken
 */

// 3. Class Design
/*
class Locker:
    - compartments: List[Compartment]
    - accessTokensMapping: Map<String, AccessToken>

    + depositPackage(size: Size) -> String AccessToken | error
    + pickup(accessToken: String) -> void | error
    + openExpiredCompartments() -> void

class AccessToken:
    - code: string
    - expiration: timestamp
    - compartment: Compartment

    + AccessToken(code, expiration, compartment)
    + isExpired() -> boolean
    + getCompartment() -> Compartment
    + getCode() -> string

class Compartment:
    - size: Size // SMALL, MEDIUM, LARGE
    - occupied: boolean

    + Compartment(size)
    + getSize() -> Size
    + isOccupied() -> boolean
    + markOccupied() -> void
    + markFree() -> void
    + open() -> void


*/

// 4.Implementation
// 4.1 Define the core logic
// 4.2 Consider the edge cases

/*
// java:
class Locker{
    // Core logic:
    // 1. Find comparmtent of the right size
    // 2. Open the compartment
    // 3. Mark this compartment as occupied
    // 4. Generate an access token
    // 5. Store the access token in a mapping for later retrieval
    // 6. Return the access token code

    // Edge cases:
    // 1. no campartments of the right size available -> throw error

    // implementation depositPackage(size: Size) -> String AccessToken | error

    public String depositPackage(Size size) throws NoAvailableCompartmentException {
        Compartment compartment = getAvailableCompartment(size);
        if (compartment == null) {
            throw new NoAvailableCompartmentException("No available compartment of size: " + size);
        }
        compartment.open();
        compartment.markOccupied();
        AccessToken token = generateAccessToken(compartment);
        accessTokensMapping.put(token.getCode(), token);
        return token.getCode();
    }

    // Core logic for getAvailableCompartment(size: Size) -> Compartment | null
    // 1. Iterate through the list of compartments
    // 2. Check if the compartment is of the right size and not occupied
    // 3. Return the first available compartment found
    private Compartment getAvailableCompartment(Size size) {
        for (Compartment compartment : compartments) {
            if (compartment.getSize() == size && !compartment.isOccupied()) {
                return compartment;
            }
        }
        return null;
    }


    // Core logic for pickup(accessToken: String) -> void | error
    // 1. Look up the access token in the mapping
    // 2. If not found, throw InvalidAccessTokenException
    // 3. If found, check if the token is expired
    // 4. If expired, throw ExpiredAccessTokenException
    // 5. If valid, open the compartment and mark it as free
    // Edge cases:
    // 1. Invalid access token -> throw InvalidAccessTokenException
    // 2. Expired access token -> throw ExpiredAccessTokenException
    // 3. Compartment is already free -> throw error (should not happen if logic is correct)
    public void pickup(String accessTokenCode) throws InvalidAccessTokenException, ExpiredAccessTokenException {
        AccessToken token = accessTokensMapping.get(accessTokenCode);
        if (token == null) {
            throw new InvalidAccessTokenException("Invalid access token: " + accessTokenCode);
        }
        if (token.isExpired()) {
            throw new ExpiredAccessTokenException("Access token expired: " + accessTokenCode);
        }
        Compartment compartment = token.getCompartment();
        compartment.open();
        compartment.markFree();
        accessTokensMapping.remove(accessTokenCode);
    }

    // Core logic for openExpiredCompartments() -> void
    // 1. Iterate through the access tokens mapping
    // 2. For each token, check if it is expired
    // 3. If expired, open the compartment and mark it as free
    public void openExpiredCompartments() {
        for (AccessToken token : accessTokensMapping.values()) {
            if (token.isExpired()) {
                Compartment compartment = token.getCompartment();
                compartment.open(); // assuming this method opens the compartment for staff to remove the package
                compartment.markFree(); // not romoving the package, just marking it as free for future use
                accessTokensMapping.remove(token.getCode());
            }
        }
     }

}




 */
// 5. Eextensions
/*
5.1 What if we want to allow a smaller package to use a larger compartment as a fallback?

5.2 How would you handle compartments that are broken or under maintenance?

5.3 How would you ensure packages are actually deposited before generating access tokens?

 */
/*
5.1 What if we want to allow a smaller package to use a larger compartment as a fallback?
    private Compartment getAvailableCompartment(Size size) {
        for (Compartment compartment : compartments) {
            if (compartment.getSize() == size && !compartment.isOccupied()) {
                return compartment;
            }
        }
        // If no exact match, allow larger compartments as fallback
        for (Compartment compartment : compartments) {
            if (compartment.getSize().isLargerThan(size) && !compartment.isOccupied()) {
                return compartment;
            }
        }
        return null;
    }

5.2 How would you handle compartments that are broken or under maintenance?
    - Add a status field to the Compartment class (e.g., AVAILABLE, OCCUPIED, BROKEN, MAINTENANCE)
    - Modify getAvailableCompartment to only consider compartments with status AVAILABLE

    enum Status {
        AVAILABLE,
        OCCUPIED,
        BROKEN,
        MAINTENANCE
    }

    private Compartment getAvailableCompartment(Size size) {
        for (Compartment compartment : compartments) {
            if (compartment.getSize() == size && compartment.getStatus() == Status.AVAILABLE) {
                return compartment;
            }
        }
        // If no exact match, allow larger compartments as fallback
        for (Compartment compartment : compartments) {
            if (compartment.getSize().isLargerThan(size) && compartment.getStatus() == Status.AVAILABLE) {
                return compartment;
            }
        }
        return null;
    }


5.3 How would you ensure packages are actually deposited before generating access tokens?
    Solution: two-phase commit
    1. reserveCompartment(size: Size) -> Compartment | error
        - Find an available compartment of the right size
        - Mark it as RESERVED (not yet occupied)
        - Return the compartment

    2. confirmDeposit(compartment: Compartment) -> AccessToken | error
        - Check if the compartment is still RESERVED
        - If yes, mark it as OCCUPIED and generate an access token


    private Compartment reserveCompartment(Size size) throws NoAvailableCompartmentException {
        Compartment compartment = getAvailableCompartment(size);
        if (compartment == null) {
            throw new NoAvailableCompartmentException("No available compartment of size: " + size);
        }
        compartment.markReserved();
        return compartment;
    }

    private AccessToken confirmDeposit(Compartment compartment) throws CompartmentNotReservedException {
        if (!compartment.isReserved()) {
            throw new CompartmentNotReservedException("Compartment is not reserved: " + compartment.getId());
        }
        compartment.markOccupied();
        AccessToken token = generateAccessToken(compartment);
        accessTokensMapping.put(token.getCode(), token);
        return token;
    }

    private AccessToken depositPackage(Size size) throws NoAvailableCompartmentException, CompartmentNotReservedException {
        Compartment compartment = reserveCompartment(size);
        // Here we would have logic to ensure the package is actually deposited
        // For example, a sensor could confirm the package is in the compartment
        boolean packageDeposited = checkPackageDeposited(compartment);
        if (!packageDeposited) {
            compartment.markAvailable(); // revert reservation
            throw new CompartmentNotReservedException("Package not deposited in compartment: " + compartment.getId());
        }
        return confirmDeposit(compartment);
    }

    enum Size {
        SMALL,
        MEDIUM,
        LARGE;

        public boolean isLargerThan(Size other) {
            return this.ordinal() > other.ordinal();
        }
    }

    enum Status {
        AVAILABLE,
        RESERVED,
        OCCUPIED,
        BROKEN,
        MAINTENANCE
    }


 */