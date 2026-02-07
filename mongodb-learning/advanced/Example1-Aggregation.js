/**
 * Example 1: Aggregation Pipeline
 * 
 * SIMPLE EXPLANATION:
 * Aggregation pipeline is like a factory assembly line.
 * Data goes through multiple stages, each doing something different,
 * and comes out transformed.
 * 
 * TECHNICAL EXPLANATION:
 * Aggregation pipeline processes documents through stages.
 * Each stage transforms documents and passes results to next stage.
 * Common stages: $match, $group, $sort, $project, $lookup, $unwind
 * 
 * INTERVIEW POINT:
 * - Pipeline stages execute in order
 * - Use $match early to reduce documents
 * - $group is powerful for summarizing data
 * - $lookup performs joins between collections
 * - Pipeline can be optimized by stage order
 */

const { connectToDatabase, closeConnection } = require('../config/database');

async function main() {
    let client;
    
    try {
        console.log('🔄 Connecting to MongoDB...\n');
        const { db, client: dbClient } = await connectToDatabase();
        client = dbClient;
        
        const ordersCollection = db.collection('orders');
        const productsCollection = db.collection('products');
        
        // Clean up
        await ordersCollection.deleteMany({});
        await productsCollection.deleteMany({});
        
        // ============================================
        // INSERT SAMPLE DATA
        // ============================================
        console.log('📝 Inserting sample data...\n');
        
        // Insert products
        const products = [
            { productId: 'P001', name: 'Laptop', category: 'Electronics', price: 999.99 },
            { productId: 'P002', name: 'Mouse', category: 'Electronics', price: 29.99 },
            { productId: 'P003', name: 'Keyboard', category: 'Electronics', price: 79.99 },
            { productId: 'P004', name: 'Desk Chair', category: 'Furniture', price: 299.99 },
            { productId: 'P005', name: 'Coffee Table', category: 'Furniture', price: 199.99 }
        ];
        await productsCollection.insertMany(products);
        
        // Insert orders
        const orders = [
            {
                orderId: 'ORD001',
                customerId: 'C001',
                customerName: 'Alice',
                date: new Date('2024-01-15'),
                items: [
                    { productId: 'P001', quantity: 1, price: 999.99 },
                    { productId: 'P002', quantity: 2, price: 29.99 }
                ],
                status: 'completed'
            },
            {
                orderId: 'ORD002',
                customerId: 'C002',
                customerName: 'Bob',
                date: new Date('2024-01-20'),
                items: [
                    { productId: 'P003', quantity: 1, price: 79.99 },
                    { productId: 'P004', quantity: 1, price: 299.99 }
                ],
                status: 'completed'
            },
            {
                orderId: 'ORD003',
                customerId: 'C001',
                customerName: 'Alice',
                date: new Date('2024-02-10'),
                items: [
                    { productId: 'P001', quantity: 1, price: 999.99 },
                    { productId: 'P005', quantity: 1, price: 199.99 }
                ],
                status: 'completed'
            },
            {
                orderId: 'ORD004',
                customerId: 'C003',
                customerName: 'Charlie',
                date: new Date('2024-02-15'),
                items: [
                    { productId: 'P002', quantity: 5, price: 29.99 },
                    { productId: 'P003', quantity: 2, price: 79.99 }
                ],
                status: 'pending'
            },
            {
                orderId: 'ORD005',
                customerId: 'C002',
                customerName: 'Bob',
                date: new Date('2024-02-20'),
                items: [
                    { productId: 'P004', quantity: 2, price: 299.99 }
                ],
                status: 'completed'
            }
        ];
        await ordersCollection.insertMany(orders);
        
        console.log('✅ Sample data inserted\n');
        
        // ============================================
        // BASIC AGGREGATION: $MATCH AND $PROJECT
        // ============================================
        console.log('🔍 === BASIC AGGREGATION ===\n');
        
        console.log('1. Find completed orders and project only orderId and customerName:');
        const completedOrders = await ordersCollection.aggregate([
            { $match: { status: 'completed' } },
            { $project: { orderId: 1, customerName: 1, date: 1, _id: 0 } }
        ]).toArray();
        
        completedOrders.forEach(order => {
            console.log(`   ${order.orderId}: ${order.customerName} - ${order.date.toISOString().split('T')[0]}`);
        });
        console.log('');
        
        // ============================================
        // $UNWIND: EXPANDING ARRAYS
        // ============================================
        console.log('📦 === $UNWIND: EXPANDING ARRAYS ===\n');
        
        console.log('2. Unwind order items to see each item separately:');
        const unwoundOrders = await ordersCollection.aggregate([
            { $match: { orderId: 'ORD001' } },
            { $unwind: '$items' },
            { $project: { orderId: 1, productId: '$items.productId', quantity: '$items.quantity', price: '$items.price', _id: 0 } }
        ]).toArray();
        
        unwoundOrders.forEach(item => {
            console.log(`   Order ${item.orderId}: ${item.productId} x${item.quantity} @ $${item.price}`);
        });
        console.log('');
        
        // ============================================
        // $GROUP: SUMMARIZING DATA
        // ============================================
        console.log('📊 === $GROUP: SUMMARIZING DATA ===\n');
        
        console.log('3. Total sales by customer:');
        const salesByCustomer = await ordersCollection.aggregate([
            { $match: { status: 'completed' } },
            { $unwind: '$items' },
            {
                $group: {
                    _id: '$customerName',
                    totalSales: { $sum: { $multiply: ['$items.quantity', '$items.price'] } },
                    orderCount: { $sum: 1 },
                    totalItems: { $sum: '$items.quantity' }
                }
            },
            { $sort: { totalSales: -1 } }
        ]).toArray();
        
        salesByCustomer.forEach(customer => {
            console.log(`   ${customer._id}: $${customer.totalSales.toFixed(2)} (${customer.orderCount} orders, ${customer.totalItems} items)`);
        });
        console.log('');
        
        // ============================================
        // $GROUP: AVERAGE AND COUNT
        // ============================================
        console.log('📈 === $GROUP: AVERAGE AND COUNT ===\n');
        
        console.log('4. Average order value by customer:');
        const avgOrderValue = await ordersCollection.aggregate([
            { $match: { status: 'completed' } },
            { $unwind: '$items' },
            {
                $group: {
                    _id: '$orderId',
                    orderTotal: { $sum: { $multiply: ['$items.quantity', '$items.price'] } },
                    customerName: { $first: '$customerName' }
                }
            },
            {
                $group: {
                    _id: '$customerName',
                    avgOrderValue: { $avg: '$orderTotal' },
                    orderCount: { $sum: 1 }
                }
            },
            { $sort: { avgOrderValue: -1 } }
        ]).toArray();
        
        avgOrderValue.forEach(customer => {
            console.log(`   ${customer._id}: $${customer.avgOrderValue.toFixed(2)} average (${customer.orderCount} orders)`);
        });
        console.log('');
        
        // ============================================
        // $LOOKUP: JOINING COLLECTIONS
        // ============================================
        console.log('🔗 === $LOOKUP: JOINING COLLECTIONS ===\n');
        
        console.log('5. Orders with product details (using $lookup):');
        const ordersWithProducts = await ordersCollection.aggregate([
            { $match: { orderId: 'ORD001' } },
            { $unwind: '$items' },
            {
                $lookup: {
                    from: 'products',
                    localField: 'items.productId',
                    foreignField: 'productId',
                    as: 'productDetails'
                }
            },
            { $unwind: '$productDetails' },
            {
                $project: {
                    orderId: 1,
                    productName: '$productDetails.name',
                    category: '$productDetails.category',
                    quantity: '$items.quantity',
                    price: '$items.price',
                    _id: 0
                }
            }
        ]).toArray();
        
        ordersWithProducts.forEach(item => {
            console.log(`   ${item.productName} (${item.category}): ${item.quantity} x $${item.price}`);
        });
        console.log('');
        
        // ============================================
        // COMPLEX AGGREGATION: MULTIPLE STAGES
        // ============================================
        console.log('🎯 === COMPLEX AGGREGATION ===\n');
        
        console.log('6. Top selling products by quantity:');
        const topProducts = await ordersCollection.aggregate([
            { $match: { status: 'completed' } },
            { $unwind: '$items' },
            {
                $group: {
                    _id: '$items.productId',
                    totalQuantity: { $sum: '$items.quantity' },
                    totalRevenue: { $sum: { $multiply: ['$items.quantity', '$items.price'] } }
                }
            },
            {
                $lookup: {
                    from: 'products',
                    localField: '_id',
                    foreignField: 'productId',
                    as: 'productInfo'
                }
            },
            { $unwind: '$productInfo' },
            {
                $project: {
                    productName: '$productInfo.name',
                    category: '$productInfo.category',
                    totalQuantity: 1,
                    totalRevenue: 1,
                    _id: 0
                }
            },
            { $sort: { totalQuantity: -1 } },
            { $limit: 5 }
        ]).toArray();
        
        topProducts.forEach((product, index) => {
            console.log(`   ${index + 1}. ${product.productName} (${product.category}): ${product.totalQuantity} sold, $${product.totalRevenue.toFixed(2)} revenue`);
        });
        console.log('');
        
        // ============================================
        // $ADD FIELDS AND $COND
        // ============================================
        console.log('➕ === $ADD FIELDS ===\n');
        
        console.log('7. Orders with calculated totals and status labels:');
        const ordersWithTotals = await ordersCollection.aggregate([
            { $unwind: '$items' },
            {
                $group: {
                    _id: '$orderId',
                    customerName: { $first: '$customerName' },
                    status: { $first: '$status' },
                    orderTotal: { $sum: { $multiply: ['$items.quantity', '$items.price'] } },
                    itemCount: { $sum: '$items.quantity' }
                }
            },
            {
                $addFields: {
                    statusLabel: {
                        $cond: {
                            if: { $eq: ['$status', 'completed'] },
                            then: '✅ Completed',
                            else: '⏳ Pending'
                        }
                    },
                    orderSize: {
                        $cond: {
                            if: { $gte: ['$orderTotal', 500] },
                            then: 'Large',
                            else: {
                                $cond: {
                                    if: { $gte: ['$orderTotal', 200] },
                                    then: 'Medium',
                                    else: 'Small'
                                }
                            }
                        }
                    }
                }
            },
            { $sort: { orderTotal: -1 } }
        ]).toArray();
        
        ordersWithTotals.forEach(order => {
            console.log(`   ${order._id}: ${order.customerName} - $${order.orderTotal.toFixed(2)} (${order.statusLabel}, ${order.orderSize})`);
        });
        console.log('');
        
        // ============================================
        // DATE OPERATIONS
        // ============================================
        console.log('📅 === DATE OPERATIONS ===\n');
        
        console.log('8. Orders by month:');
        const ordersByMonth = await ordersCollection.aggregate([
            {
                $group: {
                    _id: {
                        year: { $year: '$date' },
                        month: { $month: '$date' }
                    },
                    orderCount: { $sum: 1 },
                    totalRevenue: {
                        $sum: {
                            $reduce: {
                                input: '$items',
                                initialValue: 0,
                                in: { $add: ['$$value', { $multiply: ['$$this.quantity', '$$this.price'] }] }
                            }
                        }
                    }
                }
            },
            { $sort: { '_id.year': 1, '_id.month': 1 } }
        ]).toArray();
        
        ordersByMonth.forEach(month => {
            console.log(`   ${month._id.year}-${String(month._id.month).padStart(2, '0')}: ${month.orderCount} orders, $${month.totalRevenue.toFixed(2)}`);
        });
        console.log('');
        
        // Cleanup
        console.log('🧹 Cleaning up...');
        await ordersCollection.deleteMany({});
        await productsCollection.deleteMany({});
        console.log('✅ Cleanup complete\n');
        
        console.log('✅ Aggregation examples completed successfully!');
        console.log('\n💡 Key Takeaways:');
        console.log('   - $match early to reduce documents');
        console.log('   - $unwind expands arrays for processing');
        console.log('   - $group summarizes data');
        console.log('   - $lookup performs joins');
        console.log('   - $project shapes output');
        console.log('   - Stage order matters for performance');
        
    } catch (error) {
        console.error('❌ Error:', error.message);
        console.error(error);
    } finally {
        if (client) {
            await closeConnection(client);
        }
    }
}

// Run the example
main();

