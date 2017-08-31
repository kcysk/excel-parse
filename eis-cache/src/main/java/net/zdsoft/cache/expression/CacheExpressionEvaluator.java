package net.zdsoft.cache.expression;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;

import java.lang.reflect.Method;

/**
 * @author shenke
 * @since 2017.08.31
 */
public class CacheExpressionEvaluator {

    private ExpressionParser expressionParser;

    private DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public CacheExpressionEvaluator(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    public DefaultParameterNameDiscoverer getParameterNameDiscoverer() {
        return parameterNameDiscoverer;
    }

    public Expression getExpression(String expression) {
        return this.expressionParser.parseExpression(expression);
    }

    public Object getValue(String expression, EvaluationContext context) {
        return getExpression(expression).getValue(context);
    }

    public <T> T getValue(String expression, EvaluationContext context, Class<T> type) {
        return getExpression(expression).getValue(context, type);
    }

    public CacheEvaluationContext createEvaluationContext(Object target, Method method, Object[] args, Class<?> targetClass, Object result, BeanFactory beanFactory) {
        ExpressionRoot root = new ExpressionRoot(target, method, args, targetClass);
        CacheEvaluationContext context = new CacheEvaluationContext(target, method, args, getParameterNameDiscoverer());
        if ( result != null ) {
            context.setVariable("result", result);
        }
        context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        return context;
    }
}