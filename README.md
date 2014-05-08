OrderEgine1
===========
This is java (eclipse) project for Pizza Order Engine.
Orders are submitted in json format.

Order updates are newline-separated JSON objects, required to have at least
these three fields:
• orderId: a signed integer,
• updateId: a signed integer,
• status: one of ‘NEW’, ‘COOKING’, ‘DELIVERING’, ‘DELIVERED’,
‘REFUNDED’, or ‘CANCELED’
An update may also include additional fields, such as:
• amount: 
Orders with a NEW status will contain an amount field for the amount to be
charged (in US dollars). NEW orders without an amount field should be ignored.
An update may include additional fields, but these extra fields can be ignored.
A few example updates are below:
{"orderId": 100, "status": "NEW", "updateId": 287, "amount": 20}
{"orderId": 100, "status": "COOKING", "updateId": 289, "cookTime": 7}

Additionally, an update may be re-sent if the sender suspects you didn’t receive it
(reception is terrible in deep space, after all). Your program should only recognize
an update once. Updates are uniquely identified by (orderId, updateId); that
is, an update should not be applied to an order if you already applied an update
to that order with the same updateId. Valid updateIds (for a given orderId)
are provided in an increasing order.
Business rules
• All valid orders will begin in the NEW state.
• A NEW order can become COOKING.
• A COOKING order can become DELIVERING.
• A DELIVERING order can become DELIVERED.
• An order can become REFUNDED only if it is DELIVERED. (Pete offers
an aggressive ‘300-day delivery or you eat free’ promotion.)
• An order can become CANCELED at any time, except if it is DELIVERED
or REFUNDED.
• Updates not following these rules should be ignored.

Summary
When your program gets an EOF, it should output a summary similar to this
one and then exit:
New: 1
Cooking: 0
Delivering: 1
Delivered: 0
Canceled: 1
Refunded: 0
Total amount charged: $20
