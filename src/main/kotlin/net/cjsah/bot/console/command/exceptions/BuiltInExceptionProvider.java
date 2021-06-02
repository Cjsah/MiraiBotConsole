package net.cjsah.bot.console.command.exceptions;

public interface BuiltInExceptionProvider {
    Dynamic2CommandExceptionType doubleTooLow();

    Dynamic2CommandExceptionType doubleTooHigh();

    Dynamic2CommandExceptionType floatTooLow();

    Dynamic2CommandExceptionType floatTooHigh();

    Dynamic2CommandExceptionType integerTooLow();

    Dynamic2CommandExceptionType integerTooHigh();

    Dynamic2CommandExceptionType longTooLow();

    Dynamic2CommandExceptionType longTooHigh();

    DynamicCommandExceptionType literalIncorrect();

    Para0CommandExceptionType readerExpectedStartOfQuote();

    Para0CommandExceptionType readerExpectedEndOfQuote();

    DynamicCommandExceptionType readerInvalidEscape();

    DynamicCommandExceptionType readerInvalidBool();

    DynamicCommandExceptionType readerInvalidInt();

    Para0CommandExceptionType readerExpectedInt();

    DynamicCommandExceptionType readerInvalidLong();

    Para0CommandExceptionType readerExpectedLong();

    DynamicCommandExceptionType readerInvalidDouble();

    Para0CommandExceptionType readerExpectedDouble();

    DynamicCommandExceptionType readerInvalidFloat();

    Para0CommandExceptionType readerExpectedFloat();

    Para0CommandExceptionType readerExpectedBool();

    DynamicCommandExceptionType readerExpectedSymbol();

    Para0CommandExceptionType dispatcherUnknownCommand();

    Para0CommandExceptionType dispatcherUnknownArgument();

    Para0CommandExceptionType dispatcherExpectedArgumentSeparator();

    DynamicCommandExceptionType dispatcherParseException();

}
